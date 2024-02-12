import org.jetbrains.kotlin.build.joinToReadableString
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging
import kotlin.io.path.Path

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	id("nu.studer.jooq") version "8.2"
	id("org.flywaydb.flyway") version "10.4.1"
	id("org.siouan.frontend-jdk17") version "8.0.0"
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

frontend {
	nodeVersion.set("21.4.0")
	assembleScript.set("run build")
	packageJsonDirectory.set(File(projectDir.path, "frontend"))
	verboseModeEnabled.set(true)
}

tasks.register<Copy>("processFrontendResources") {
	// Directory containing the artifacts produced by the frontend project
	val frontendProjectBuildDir = file("${layout.projectDirectory}/frontend/dist")
	// Directory where the frontend artifacts must be copied to be packaged alltogether with the backend by the 'war'
	// plugin.
	val frontendResourcesDir = file("${layout.projectDirectory}/src/main/resources/static")

	group = "Frontend"
	description = "Process frontend resources"
	dependsOn(":assembleFrontend")

	from(frontendProjectBuildDir)
	into(frontendResourcesDir)
}

tasks.named<Task>("processResources") {
	dependsOn("processFrontendResources")
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
