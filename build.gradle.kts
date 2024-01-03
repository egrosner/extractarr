import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("nu.studer.jooq") version "8.2"
	id("org.flywaydb.flyway") version "10.4.1"
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

	//flyaway
	implementation("org.flywaydb:flyway-core")
	jooqGenerator("org.xerial:sqlite-jdbc:3.44.1.0")
}

flyway {
	url = "jdbc:sqlite:$projectDir/config/extractarr.db"
	user = ""
	password = ""
	baselineOnMigrate = true
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
