#!/usr/bin/env groovy

def dockerNetworkCreate(name) {
	dockerNetworkRemove(name)
	sh "docker network create ${name}"
}

def dockerNetworkRemove(name) {
	sh "docker network rm ${name} || true"
}

def dockerDBRun(hostname, password, network) {
	dockerDBRemove(hostname)
	sh "docker run --name ${hostname} --network ${network} -e MYSQL_ROOT_PASSWORD=${password} -d mysql:5.7"
}

def dockerDBRemove(hostname) {
	sh "docker stop ${hostname} || true"
	sh "docker rm ${hostname} || true"
}

def dockerVolumeRemove() {
	sh "docker volume prune -f"
}

def wipeWorkspaces(WORKSPACE)
{
	dir('/var/lib/jenkins/workspace'){
		sh 'pwd'
		sh 'find -maxdepth 1 -name $(basename ${WORKSPACE})@\\* ! -name $(basename ${WORKSPACE})@tmp'

		sh 'sudo rm -rf -- $(basename ${WORKSPACE})_*/'
		// Removes the clones & tmp folders created for parallelization
		sh 'find -maxdepth 1 -name $(basename ${WORKSPACE})@\\* ! -name $(basename ${WORKSPACE})@tmp -exec sudo rm -rf {} \\;'
	}

	// Wipes the main workspace afterwards leaving an empty dir
	deleteDir()
}

