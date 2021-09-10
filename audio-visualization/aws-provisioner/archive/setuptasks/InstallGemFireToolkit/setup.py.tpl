import os.path
import shutil
import subprocess

if __name__ == '__main__':
   {% if Servers[ServerNum].Installations[InstallationNum].Owner %}
   owner = '{{ Servers[ServerNum].Installations[InstallationNum].Owner }}'
   {% else %}
   owner = ''
   {% endif %}
   cluster_home = '{{ Servers[ServerNum].Installations[InstallationNum].ClusterHome }}'

   mvnEnv = dict()
   mvnEnv['JAVA_HOME'] = '/etc/alternatives/java_sdk_1.8.0'

   if len(owner) > 0:
      subprocess.check_call(['sudo','-u',owner,'-E', '/runtime/maven/bin/mvn','-DskipTests','package'], cwd='/tmp/setup' , env=mvnEnv)
   else:
      subprocess.check_call(['/runtime/maven/bin/mvn','-DskipTests','package'], cwd='/tmp/setup' , env=mvnEnv)

   print 'built gemfire-toolkit'

   subprocess.check_call(['tar','-xzf', '/tmp/setup/target/gemfire-toolkit-N-runtime.tar.gz', '-C', cluster_home])

   if len(owner) > 0:
      subprocess.check_call(['chown', '-R', '{0}:{0}'.format(owner), os.path.join(cluster_home, 'gemtools')])

   print 'installed gemfire-toolkit'
