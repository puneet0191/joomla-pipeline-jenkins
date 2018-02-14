#!/usr/bin/env groovy

def call(Map args) {
    if (args.action == 'check') {
        return check()
    }
    if (args.action == 'postProcess') {
        return postProcess()
    }
    error 'jenkins skip build has been called due to branch indexing'
}

def check() {
    env.CI_SKIP = "false"
    result = sh (script: "git log -1 | grep '.*Branch indexing*'", returnStatus: true)
    if (result == 0) {
        env.CI_SKIP = "true"
        error "'Branch indexing' found in git commit message. Aborting."
    }
}

def postProcess() {
    if (env.CI_SKIP == "true") {
        currentBuild.result = 'NOT_BUILT'
    }
}