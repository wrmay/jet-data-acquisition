#
# Copyright (c) 2015-2016 Pivotal Software, Inc. All Rights Reserved.
#
import json
import os
import os.path
import pwd
import shutil
import subprocess

def basename(url):
    i = url.rindex('/')
    return url[i+1:]

def runQuietly(*args):
    p = subprocess.Popen(list(args), stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
    output = p.communicate()
    if p.returncode != 0:
        raise Exception('"{0}" failed with the following output: {1}'.format(' '.join(list(args)), output[0]))

if __name__ == '__main__':
    ip = '{{Servers[ServerNum].PublicIpAddress }}'

    # use git to download gemfire-manager
    runQuietly('git','clone','https://github.com/Pivotal-Data-Engineering/gemfire-manager.git', '/tmp/setup/gemfire-manager')

    #build it
    mvnEnv = dict()
    mvnEnv['JAVA_HOME'] = '/runtime/java'
    subprocess.check_call(['/runtime/maven/bin/mvn','-DskipTests', 'packge'],cwd='/tmp/setup/gemfire-manager/gemfire-toolkit', env=mvnEnv)

    # locate the parent of cluster-home
    # copy the gemfire-manager scripts into cluster home
    # move  cluster.json into cluster-home
    # unpack gemtools into cluster-home

    with open('/tmp/setup/cluster.json','r') as f:
        config = json.load(f)

    clusterHome = '{{ Servers[ServerNum].Installations[InstallationNum].ClusterHome }}'
    clusterParent = os.path.dirname(clusterHome)

    {% if "AWSAccessKeyId" in Servers[ServerNum].Installations[InstallationNum] %}

    AWS_ACCESS_KEY_ID = '{{ Servers[ServerNum].Installations[InstallationNum].AWSAccessKeyId }}'
    AWS_SECRET_ACCESS_KEY = '{{ Servers[ServerNum].Installations[InstallationNum].AWSSecretAccessKey }}'
    AWS_S3_BUCKET_REGION = '{{ Servers[ServerNum].Installations[InstallationNum].AWSS3Region }}'
    runQuietly('aws', 'configure', 'set', 'aws_access_key_id', AWS_ACCESS_KEY_ID)
    runQuietly('aws', 'configure', 'set', 'aws_secret_access_key', AWS_SECRET_ACCESS_KEY)
    runQuietly('aws', 'configure', 'set', 'default.region', AWS_S3_BUCKET_REGION)

    {% endif %}

    if not os.path.exists(clusterHome):
      os.makedirs(clusterHome)

    for script in ['cluster.py', 'gf.py', 'clusterdef.py','gemprops.py']:
      shutil.copy(os.path.join('/tmp/setup/gemfire-manager',script),clusterHome)

    #unpack the gemfire-toolkit
    runQuietly('tar', '-C', clusterHome, '-xzf', '/tmp/setup/gemfire-manager/gemfire-toolkit/target/gemfire-toolkit-N-runtime.tar.gz')

    # copy the cluster definition file
    shutil.copy('/tmp/setup/cluster.json', clusterHome)

    # and the config directory if it exists
    if os.path.exists('/tmp/setup/config'):
      targetDir = os.path.join(clusterHome,'config')
      if os.path.exists(targetDir):
         shutil.rmtree(targetDir)

      shutil.copytree('/tmp/setup/config',targetDir)

    # change the owner
    runQuietly('chown', '-R', '{0}:{0}'.format('{{ Servers[ServerNum].SSHUser }}'), clusterHome)
    print '{0} gemfire cluster set up at {1}'.format(ip, clusterHome)
