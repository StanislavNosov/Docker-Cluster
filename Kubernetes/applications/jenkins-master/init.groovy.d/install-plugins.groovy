import jenkins.model.*
import hudson.model.*;
def instance = Jenkins.getInstance()
def activatePlugin(plugin) {
        if (! plugin.isEnabled()) {
        plugin.enable()
        deployed = true
        }
        plugin.getDependencies().each {
            activatePlugin(pm.getPlugin(it.shortName))
        }
    }
pm = instance.pluginManager
uc = instance.updateCenter
pm.plugins.each { 
    plugin ->plugin.disable()
    }

deployed = false

["git", "active-directory","role-strategy","github","gerrit-trigger","email-ext","ssh-slaves"].each {
if (! pm.getPlugin(it)) {
  println("Installing plugin $it")
  deployment = uc.getPlugin(it).deploy()
  deployment.get()

}
activatePlugin(pm.getPlugin(it))
}

if (deployed) {
println("saving Jenkins configuration!")
instance.save()
}

