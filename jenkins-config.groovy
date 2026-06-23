import jenkins.model.Jenkins
import hudson.tools.InstallSourceProperty
import hudson.model.JDK
import hudson.tasks.Maven
import hudson.tools.InstallSourceProperty
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.domains.Domain
import hudson.util.Secret

// 1. Configure JDK 17
Jenkins.instance.getJDKs().clear()
def jdkInstaller = new hudson.tools.JDKInstaller(null)
def jdkProps = new InstallSourceProperty([jdkInstaller])
def jdk = new JDK("Java17", null, [jdkProps])
Jenkins.instance.getJDKs().add(jdk)
println "JDK 17 configured: ${jdk.name}"

// 2. Configure Maven 3.9.9
def mvnInstaller = new Maven.MavenInstaller("3.9.9")
def mvnProps = new InstallSourceProperty([mvnInstaller])
def mvn = new Maven.MavenInstallation("Maven", null, [mvnProps])
Jenkins.instance.getDescriptor("hudson.tasks.Maven\$MavenInstallation\$DescriptorImpl").setInstallations(mvn)
println "Maven 3.9.9 configured: ${mvn.name}"

// 3. Create GitHub credentials
def store = Jenkins.instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()
def domain = Domain.global()
def creds = store.getCredentials(domain)
def existing = creds.find { it.id == "gitcred" }
if (existing) {
    store.removeCredentials(domain, existing)
}
def gitCred = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "gitcred", "GitHub credentials for capstoneproject", "ramesh734", "ghp_placeholder")
store.addCredentials(domain, gitCred)
println "GitHub credentials 'gitcred' created"

Jenkins.instance.save()
println "All configurations saved successfully!"
