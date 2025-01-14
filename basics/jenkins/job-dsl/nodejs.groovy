job('NodeJS example') {
    scm {
        git('git://github.com/poladinitz/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("npm install")
    }
}

job('NodeJS Docker example') {
    scm {
        git('git://github.com/poladinitz/docker-cicd.git') {  node -> // is hudson.plugins.git.GitSCM
            node / gitConfigName('DSL User')
            node / gitConfigEmail('jenkins-dsl@newtech.academy')
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
    wrappers {
        nodejs('nodejs') 
    }
    steps {
        dockerBuildAndPublish {
            repositoryName('poladinitz/docker-cicd') //qa / dev
            tag('${GIT_REVISION,length=9}')
            registryCredentials('docker-hub')
            forcePull(false)
            forceTag(false)
            createFingerprints(false)
            skipDecorate()
            buildContext('./basics')
        }
    }
}

pipelineJob('DSL-pipeline') {
    definition {
        cpsScm {
            scm {
                git {
                    remote{url('git://github.com/poladinitz/docker-cicd.git')}
                    branches('master', '**/feature*')
                    scriptPath('./basics/misc/Jenkinsfile')
                }
            }
        }
    }
    triggers {
        scm('H/5 * * * *')
    }
}
