import hudson.model.JDK
import hudson.tasks.Maven
import hudson.tools.InstallSourceProperty
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.domains.Domain

// 1. Configure JDK 17
def jdk = new JDK("Java17", "/usr/lib/jvm/java-17-openjdk", null)
jenkins.model.Jenkins.instance.getJDKs().add(jdk)
println "JDK 17 configured"

// 2. Configure Maven
def mvnInstaller = new Maven.MavenInstaller("3.9.9")
def mvnProps = new InstallSourceProperty([mvnInstaller])
def mvn = new Maven.MavenInstallation("Maven", null, [mvnProps])
jenkins.model.Jenkins.instance.getDescriptorByType(Maven.MavenInstallation.DescriptorImpl.class).setInstallations(mvn)
println "Maven 3.9.9 configured"

// 3. GitHub credentials
def store = jenkins.model.Jenkins.instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()
def domain = Domain.global()
def existing = store.getCredentials(domain).find { it.id == "gitcred" }
if (existing) { store.removeCredentials(domain, existing) }
def gitCred = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "gitcred", "GitHub credentials for capstoneproject", "ramesh734", "token_placeholder")
store.addCredentials(domain, gitCred)
println "GitHub credentials gitcred created"

jenkins.model.Jenkins.instance.save()
println "ALL DONE"