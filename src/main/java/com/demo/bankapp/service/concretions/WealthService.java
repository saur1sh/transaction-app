package com.demo.bankapp.service.concretions;

import com.demo.bankapp.configuration.Constants;
import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.exception.InsufficientFundsException;
import com.demo.bankapp.exception.UserNotFoundException;
import com.demo.bankapp.model.User;
import com.demo.bankapp.model.Wealth;
import com.demo.bankapp.repository.UserRepository;
import com.demo.bankapp.repository.WealthRepository;
import com.demo.bankapp.service.abstractions.IWealthService;
import com.demo.bankapp.util.FetchExchangeRatesHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class WealthService implements IWealthService {

	private final WealthRepository wealthRepository;
	private final UserRepository userRepository;
	private final FetchExchangeRatesHelper fetchExchangeRatesHelper;

	@Override
	public void newWealthRecord(Long userId) {
		User userEntity = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

		Map<String, BigDecimal> wealthMap = new HashMap<>();

		Map<String, Double> rates = getCurrencyRates();

		wealthMap.put(Constants.MAIN_CURRENCY, BigDecimal.ZERO);

		for (Map.Entry<String, Double> entry : rates.entrySet()) {
			wealthMap.put(entry.getKey(), BigDecimal.ZERO);
		}

		addInitialBalance(wealthMap);

		// 2. Pass the User entity to the Wealth constructor
		Wealth userWealth = new Wealth(userEntity, wealthMap);

		wealthRepository.save(userWealth);
	}

	@Override
	public Map<String, Double> getCurrencyRates() {
		return fetchExchangeRatesHelper.getCurrencyRates();
	}

	@Override
	public void makeWealthExchange(Long userId, String currency, BigDecimal amount, boolean isBuying) {
		Wealth userWealth = wealthRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Map<String, BigDecimal> wealthMap = userWealth.getWealthMap();
		String mainCurrency = Constants.MAIN_CURRENCY;

		if (!wealthMap.containsKey(currency)) {
			throw new BadRequestException("Invalid currency.");
		}

		// Fetch rates once and use helper method to get the rate, handling conversion from API base (EUR)
		Map<String, Double> rates = getCurrencyRates();
		BigDecimal rate = getRateInMainCurrency(rates, currency, mainCurrency);

		// Calculate required amount in the main currency (TRY equivalent)
		// This calculation depends on whether the MAIN_CURRENCY is the API Base or not.
		BigDecimal tryEquivalent = amount.multiply(rate); // Assuming rate is 1 TRY = X Currency

		BigDecimal mainBalance = wealthMap.get(mainCurrency);
		BigDecimal currencyBalance = wealthMap.get(currency);

		// --- 1. Check for Insufficient Funds ---
		if (isBuying) {
			// Buying requires sufficient MAIN_CURRENCY balance (TRY)
			if (tryEquivalent.compareTo(mainBalance) > 0) {
				throw new InsufficientFundsException("Insufficient funds in " + mainCurrency + ".");
			}
		} else {
			// Selling requires sufficient target currency balance
			if (amount.compareTo(currencyBalance) > 0) {
				throw new InsufficientFundsException(currency);
			}
		}

		// --- 2. Update Balances ---
		if (isBuying) {
			// Decrease MAIN_CURRENCY, Increase Target Currency
			wealthMap.put(mainCurrency, mainBalance.subtract(tryEquivalent));
			wealthMap.put(currency, currencyBalance.add(amount));
		} else {
			// Decrease Target Currency, Increase MAIN_CURRENCY
			wealthMap.put(currency, currencyBalance.subtract(amount));
			wealthMap.put(mainCurrency, mainBalance.add(tryEquivalent));
		}

		userWealth.setWealthMap(wealthMap);
		wealthRepository.save(userWealth);
	}

	@Override
	public void makeWealthTransaction(Long userId, String currency, BigDecimal amount, boolean isIncrementing) {
		Wealth userWealth = wealthRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Map<String, BigDecimal> wealthMap = userWealth.getWealthMap();

		if (!wealthMap.containsKey(currency)) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDCURRENCY);
		}

		BigDecimal currentBalance = wealthMap.get(currency);

		if (!isIncrementing) { // Decrementing (Withdrawal/Debit)
			if (amount.compareTo(currentBalance) > 0) {
				throw new InsufficientFundsException(currency);
			}
			wealthMap.put(currency, currentBalance.subtract(amount));
		} else { // Incrementing (Deposit/Credit)
			wealthMap.put(currency, currentBalance.add(amount));
		}

		userWealth.setWealthMap(wealthMap);
		wealthRepository.save(userWealth);
	}

	@Override
	public Wealth findWealth(Long userId) {
		return wealthRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}



	private void addInitialBalance(Map<String, BigDecimal> wealthMap) {
		// Uses BigDecimal(String) constructor to avoid floating point issues
		BigDecimal amountToAdd = new BigDecimal("150000.00");

		// Assuming Constants.MAIN_CURRENCY is already initialized to ZERO in wealthMap
		wealthMap.computeIfPresent(Constants.MAIN_CURRENCY, (k, v) -> v.add(amountToAdd));
	}

	/**
	 * Helper to get the rate for the target currency relative to the MAIN_CURRENCY (TRY).
	 * Assumes API base is EUR.
	 * Rate = (Rate of Currency / Rate of TRY)
	 */
	private BigDecimal getRateInMainCurrency(Map<String, Double> rates, String targetCurrency, String mainCurrency) {
		Double tryRate = rates.get(mainCurrency);
		Double targetRate = rates.get(targetCurrency);

		if (tryRate == null || targetRate == null) {
			throw new BadRequestException("Missing exchange rate for " + mainCurrency + " or " + targetCurrency);
		}

		// Rate of target currency relative to TRY: Rate(Target)/Rate(TRY)
		// This gives the value of 1 TRY in the target currency.
		BigDecimal rate = BigDecimal.valueOf(targetRate)
				.divide(BigDecimal.valueOf(tryRate), 9, RoundingMode.HALF_UP);

		return rate;
	}
}