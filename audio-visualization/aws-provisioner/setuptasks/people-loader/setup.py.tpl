import os.path
import shutil
import subprocess

if __name__ == '__main__':
   targetDir = '{{ Servers[ServerNum].Installations[InstallationNum].TargetDir }}'
   owner = '{{ Servers[ServerNum].Installations[InstallationNum].Owner }}'

   if os.path.exists(targetDir):
      shutil.rmtree(targetDir)
      print 'removed {0}'.format(targetDir)

   subprocess.check_call(['git','clone','https://github.com/Pivotal-Data-Engineering/people-loader.git', '/tmp/setup/people-loader'])
   shutil.copytree('/tmp/setup/people-loader',targetDir)
   subprocess.check_call(['chown','-R',owner,targetDir])
   print 'copied people-loader to {0}'.format(targetDir)

   mvnEnv = dict()
   mvnEnv['JAVA_HOME'] = '/etc/alternatives/java_sdk_1.8.0'
   subprocess.check_call(['sudo','-u',owner,'-E', '/runtime/maven/bin/mvn','clean', 'install'],cwd=targetDir, env=mvnEnv)
   print 'built people-loader'
