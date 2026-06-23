import jenkins.model.Jenkins
import hudson.tools.InstallSourceProperty
import hudson.model.JDK
import hudson.tasks.Maven
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.domains.Domain

Jenkins.instance.getJDKs().clear()
def jdkInstaller = new hudson.tools.JDKInstaller(null)
def jdkProps = new InstallSourceProperty([jdkInstaller])
def jdk = new JDK("Java17", null, [jdkProps])
Jenkins.instance.getJDKs().add(jdk)
println "JDK 17 configured"

def mvnInstaller = new Maven.MavenInstaller("3.9.9")
def mvnProps = new InstallSourceProperty([mvnInstaller])
def mvn = new Maven.MavenInstallation("Maven", null, [mvnProps])
Jenkins.instance.getDescriptor("hudson.tasks.Maven\$MavenInstallation\$DescriptorImpl").setInstallations(mvn)
println "Maven 3.9.9 configured"

def store = Jenkins.instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()
def domain = Domain.global()
def existing = store.getCredentials(domain).find { it.id == "gitcred" }
if (existing) { store.removeCredentials(domain, existing) }
def gitCred = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "gitcred", "GitHub credentials for capstoneproject", "ramesh734", "token_placeholder")
store.addCredentials(domain, gitCred)
println "GitHub credentials gitcred created"
Jenkins.instance.save()
println "ALL DONE"