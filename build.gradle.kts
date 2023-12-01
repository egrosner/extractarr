import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("nu.studer.jooq") version "8.2"
	id("org.jetbrains.gradle.liquibase") version "1.6.0-RC.5"
}

group = "com.erich.grosner"
version = "1.0.2"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	//test
	testImplementation("io.mockk:mockk:1.13.8")

	//database stuff
	runtimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	jooqGenerator("org.xerial:sqlite-jdbc:3.44.1.0")
	implementation("org.liquibase:liquibase-core")

	//liquibase gradle stuff
	liquibaseRuntime("org.liquibase:liquibase-core:4.16.1")
	liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:3.0.2")
	liquibaseRuntime("org.xerial:sqlite-jdbc:3.44.1.0")
}

liquibase {
	activities {
		all {
			properties {
				changeLogFile.set("src/main/resources/db/changelog/db.changelog-master.yaml")
				driver.set("org.sqlite.JDBC")
			}
		}
		register("local_sqlite") {
			properties {
				url.set("jdbc:sqlite:./config/extractarr.db")
				password.set("")
				username.set("")
			}
		}
	}
}

jooq {
	version.set("3.18.2")

	configurations {
		create("main") {
			generateSchemaSourceOnCompilation.set(false)
			jooqConfiguration.apply {
				logging = Logging.WARN
				jdbc.apply {
					driver = "org.sqlite.JDBC"
					url = "jdbc:sqlite:./config/extractarr.db"
				}
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}
