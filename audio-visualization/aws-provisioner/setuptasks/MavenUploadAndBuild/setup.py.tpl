import os.path
import shutil
import subprocess

if __name__ == '__main__':
   targetDir = '{{ Servers[ServerNum].Installations[InstallationNum].TargetDir }}'
   owner = '{{ Servers[ServerNum].Installations[InstallationNum].Owner }}'
   buildTargets = ['clean','install']

   {% if Servers[ServerNum].Installations[InstallationNum].BuildTargets %}
   buildTargets = [ {% for tgt in Servers[ServerNum].Installations[InstallationNum].BuildTargets -%}"{{ tgt }}"{% if not loop.last -%},{%- endif %} {% endfor %} ]
   {% endif %}

   if os.path.exists(targetDir):
      shutil.rmtree(targetDir)
      print 'removed {0}'.format(targetDir)

   shutil.copytree('/tmp/setup',targetDir)
   if len(owner) > 0:
      subprocess.check_call(['chown','-R',owner,targetDir])

   print 'copied maven project to {0}'.format(targetDir)

   mvnEnv = dict()
   mvnEnv['JAVA_HOME'] = '/runtime/java'

   if len(owner) > 0:
      subprocess.check_call(['sudo','-u',owner,'-E', '/runtime/maven/bin/mvn','-DskipTests'] + buildTargets,cwd=targetDir, env=mvnEnv)
   else:
      subprocess.check_call(['/runtime/maven/bin/mvn','-DskipTests'] + buildTargets,cwd=targetDir, env=mvnEnv)

   print 'built maven project'
