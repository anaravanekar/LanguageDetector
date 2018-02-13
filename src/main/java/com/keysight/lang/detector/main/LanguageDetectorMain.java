package com.keysight.lang.detector.main;

import com.google.common.base.Optional;
import com.keysight.lang.detector.model.MDMAccount;
import com.keysight.lang.detector.model.MDMAccountAddress;
import com.keysight.lang.detector.repo.MDMAccountAddressRepository;
import com.keysight.lang.detector.repo.MDMAccountRepository;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

@SpringBootApplication
@EnableJpaRepositories("com.keysight.lang")
@ComponentScan({"com.keysight.lang"})
@EntityScan("com.keysight.lang")
public class LanguageDetectorMain implements CommandLineRunner  {
	private static final Logger log = LoggerFactory.getLogger(LanguageDetectorMain.class);
	private static final int pageSize = 100000;
	@Resource
	private Environment environment;
	@Resource
	MDMAccountRepository repository;

	@Resource
	MDMAccountAddressRepository addressRepository;

	public MDMAccountAddressRepository getAddressRepository() {
		return addressRepository;
	}

	public void setAddressRepository(MDMAccountAddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LanguageDetectorMain.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		//load all languages:
		List<LanguageProfile> languageProfiles = null;

		try {
			languageProfiles = new LanguageProfileReader().readAllBuiltIn();
			//build language detector:
			LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
					.withProfiles(languageProfiles)
					.build();
			updateLocaleForAccounts(languageDetector);
			updateLocaleForAccountAddresses(languageDetector);

		} catch (IOException e) {
			log.error("Error while loading all Language Profiles");
		}
	}

	private void updateLocaleForAccountAddresses(
			LanguageDetector languageDetector) {
		List<MDMAccountAddress> addresses = null;
		int pageNumber=0;
		do {
			Page<MDMAccountAddress> pageResult = (Page<MDMAccountAddress>) addressRepository.findAll(new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "rowNumber"));
			addresses = pageResult!=null?pageResult.getContent():null;
			log.info("pageResult:"+pageResult);
			log.info("addresses:"+addresses);
			pageNumber++;
			List<MDMAccountAddress> updatedAddresses = new ArrayList<>();
			if(addresses!=null && !addresses.isEmpty()) {
				//addresses.stream().filter(Objects::nonNull).forEach(a -> {
				log.info("fetched addresses:"+addresses.size());
				for(MDMAccountAddress a:addresses){
					log.info("Address Info:" + a);
					if(a!=null){
						String textForDetection = a.getAddress()!=null?a.getAddress().trim():"";
						log.info("textForDetection:" + textForDetection);
						String locale = getLocale(languageDetector, textForDetection);
						log.debug("Detected Locale:" + locale);
						if (null != locale && ("ko".equalsIgnoreCase(locale) || "ja".equalsIgnoreCase(locale) || "zh-cn".equalsIgnoreCase(locale))) {
							a.setLocale("ja");
							updatedAddresses.add(a);
						}
					}
				//});
				}
				addressRepository.save(updatedAddresses);
				log.info("Page {}. Updated {} records with locale ja", pageNumber,updatedAddresses.size());
			}
		}while(addresses!=null && !addresses.isEmpty());
	}

	private void updateLocaleForAccounts(LanguageDetector languageDetector) {
		List<MDMAccount> accounts = null;
		int pageNumber=0;
		do {
			Page<MDMAccount> pageResult = (Page<MDMAccount>) repository.findAll(new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "systemId"));
			accounts = pageResult!=null?pageResult.getContent():null;
			pageNumber++;
			List<MDMAccount> updatedAccounts = new ArrayList<>();
			if(accounts!=null && !accounts.isEmpty()) {
				accounts.stream().filter(Objects::nonNull).forEach(a -> {
					//log.debug("Account Info:" + a);
					String locale = getLocale(languageDetector, a.getAccountName().trim());
					//log.debug("Detected Locale:" + locale);
					if (null != locale && ("ko".equalsIgnoreCase(locale) || "ja".equalsIgnoreCase(locale) || "zh-cn".equalsIgnoreCase(locale))) {
						a.setLocale("ja");
						updatedAccounts.add(a);
					}
				});
				repository.save(updatedAccounts);
				log.info("Updated {} records with locale ja", updatedAccounts.size());
			}
		}while(accounts!=null && !accounts.isEmpty());
	}

	private String getLocale(LanguageDetector languageDetector, String text) {
		//english, german, french, polish, russian, asian(japanese, korean (simplified, traditional chinese)

		/**
		 * Invoke Language detector library to find out the locale & language
		 *
		 */
		String detectedLanguage = null;

		//create a text object factory
		TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

		TextObject textObject = textObjectFactory.forText(text);
		//Optional<LdLocale> lang = languageDetector.detect(textObject);
		double minimalConfidence = Double.valueOf(environment.getProperty("locale.minimal.confidence"));
		Optional<LdLocale> lang = languageDetector.detectWithMinimalConfidence(minimalConfidence,textObject);
		if(lang.isPresent()){
			LdLocale ldLocale = lang.get();
			detectedLanguage = ldLocale.getLanguage();
//			log.info("Detected Locale: "+ ldLocale);
		}else{
//			log.error("Language could not be detected. May be because of probability of detected language is less than minimal confidence 0.999");
		}

		return detectedLanguage;
	}


	public MDMAccountRepository getRepository() {
		return repository;
	}

	@Autowired
	public void setRepository(MDMAccountRepository repository) {
		this.repository = repository;
	}

}
