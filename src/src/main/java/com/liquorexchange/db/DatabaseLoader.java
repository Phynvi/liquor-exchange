package com.liquorexchange.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.liquorexchange.db.repository.*;
import com.liquorexchange.db.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * If com.liquorexchange.resetdb in application.properties is set to true,
 * all data in database will be deleted and new fresh set of test data will be created
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Country CZECH_REPUBLIC = new Country("CZ", "Czech Republic");
    private static final Country UNITED_STATES = new Country("US", "United States");

    private static final Category WINE = new Category(null, "Wine");
    private static final Category BOURBON = new Category(null, "Bourbon");

	private static final Role ADMIN = new Role(RoleName.ROLE_ADMIN);
	private static final Role USER = new Role(RoleName.ROLE_USER);

	private static final User TEST_USER = new User(
			"user@email.com",
			new UserInfo("Test","User"),
			"12345",
			new Calendar.Builder().setDate(1995, Calendar.MAY, 5).build().getTime(),
			"5th Avenue, 500000 NYC",
			"5th Avenue, 500000 NYC",
			"GM",
			123456,
			1234567
	);

	private static final UserSecurity TEST_USER_SEC = UserSecurity.create(
			TEST_USER,
			"password",
			new ArrayList<Role>() {{
				add(USER);
			}}
	);

    private static final User JOHN = new User(
		"johny@gmail.com",
		new UserInfo("John","Parker"),
		"12345",
		new Calendar.Builder().setDate(1995, Calendar.MAY, 5).build().getTime(),
		"5th Avenue, 500000 NYC",
		"5th Avenue, 500000 NYC",
		"GM",
		123456,
		1234567
    );

	private static final UserSecurity JOHN_SEC = UserSecurity.create(
		JOHN,
		"john",
		new ArrayList<Role>() {{
			add(ADMIN);
			add(USER);
		}}
	);

    private static final User ANNA = new User(
		"anna@gmail.com",
		new UserInfo("Anna","Parker"),
		"12345",
		new Calendar.Builder().setDate(1995, Calendar.MAY, 5).build().getTime(),
		"5th Avenue, 500000 NYC",
		"5th Avenue, 500000 NYC",
		"GM",
		123456,
		1234567
    );

	private static final UserSecurity ANNA_SEC = UserSecurity.create(
		ANNA,
		"anna",
		new ArrayList<Role>() {{
			add(USER);
		}}
	);

    @Value("${com.liquorexchange.resetdb}")
	private boolean reset_db;

	private final CountryRepository countryRepository;
	private final AuctionRepository auctionRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final UserSecurityRepository userSecurityRepository;
    private final RoleRepository roleRepository;

	@Autowired
	public DatabaseLoader(
	        CountryRepository countryRepository,
            AuctionRepository auctionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
			UserSecurityRepository userSecurityRepository,
            RoleRepository roleRepository
    ) {
		this.countryRepository = countryRepository;
		this.auctionRepository = auctionRepository;
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
		this.userSecurityRepository = userSecurityRepository;
        this.roleRepository = roleRepository;
	}

	/**
	 * Find out whether database is empty of data.
	 * @return True if database is assumed empty.
	 */
	private boolean isEmpty()
	{
		return (this.roleRepository.count() == 0) && (this.userSecurityRepository.count() == 0);
	}

	@Override
	public void run(String... strings) {
		// DELETE EVERYTHING!
		if (this.reset_db) {
			this.countryRepository.deleteAll();
			this.auctionRepository.deleteAll();
			this.categoryRepository.deleteAll();
			this.userRepository.deleteAll();
			this.userSecurityRepository.deleteAll();
			this.roleRepository.deleteAll();
		}

		// INSERT EVERYTHING (if db is empty)
		if (this.isEmpty()) {

			this.roleRepository.save(ADMIN);
			this.roleRepository.save(USER);

			this.userRepository.save(JOHN);
			this.userRepository.save(ANNA);
			this.userRepository.save(TEST_USER);

			this.userSecurityRepository.save(JOHN_SEC);
			this.userSecurityRepository.save(ANNA_SEC);
			this.userSecurityRepository.save(TEST_USER_SEC);

			this.countryRepository.save(CZECH_REPUBLIC);
			this.countryRepository.save(UNITED_STATES);

			this.categoryRepository.save(WINE);
			this.categoryRepository.save(BOURBON);

			ArrayList<String> beverages = new ArrayList<String>(Arrays.asList(
				"Riesling, archive 2002",
				"Chardonnay, old archive",
				"Sauvignon blanc from 1983",
				"Syrah, rustical",
				"Pinot noir",
				"Presidente Brandy",
				"E & J. Calvados Bushnell",
				"Germain-Robin Brandy. Ansac Cognac",
				"Courvoisier Cognac",
				"Asbach Uralt Brandy"
			));

			for (int i = 1; i <= 5; i++) {
				this.auctionRepository.save(new Auction(
					null,
					beverages.get(i % beverages.size()),
					"ACTIVE",
					"GOOD",
					"Scratches on paper box",
					1,
					0.7,
					Date.from(Instant.now()),
					Date.from(Instant.now().plusSeconds(5 * 3600)),
					ANNA.getInfo(),
					JOHN.getInfo(),
					700,
					800,
					5,
					CZECH_REPUBLIC,
					WINE
                ));
			}

			for (int i = 6; i <= 10; i++) {
				this.auctionRepository.save(new Auction(
					null,
					beverages.get(i % beverages.size()),
					"ACTIVE",
					"VERY GOOD",
					"As new",
					2,
					0.5,
					Date.from(Instant.now()),
					Date.from(Instant.now().plusSeconds(6 * 3600)),
					JOHN.getInfo(),
					ANNA.getInfo(),
					1000,
					1255,
					10,
					UNITED_STATES,
					BOURBON
                ));
			}

		}

	}
}
