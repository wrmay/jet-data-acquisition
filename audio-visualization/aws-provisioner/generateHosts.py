#!python
#
# Copyright (c) 2015-2016 Pivotal Software, Inc. All Rights Reserved.
#
import jinja2
import jinja2.filters
import json
import os.path
import shutil
import sys

if __name__ == '__main__':
    #now dir should be set
    gemopsBase = os.path.dirname(os.path.abspath(sys.argv[0]))
    awsClusterFile = os.path.join(gemopsBase,'config','awscluster.json')
    
    awsRuntimeFile = os.path.join(gemopsBase,'aws_runtime.json')

    if not os.path.isfile(awsClusterFile):
        sys.exit('missing required file: "{0}"'.format(awsClusterFile))

    if not os.path.isfile(awsRuntimeFile):
        sys.exit('missing required file: "{0}"'.format(awsRuntimeFile))


    #initializeRefData()
    #templateDir = os.path.join(gemopsBase, 'templates')
    #templateFileName = 'env.json.tpl'

    with open(awsClusterFile, 'r') as awsClusterFile:
        awsCluster = json.load(awsClusterFile)

    with open(awsRuntimeFile, 'r') as awsRuntimeFile:
        awsRuntime = json.load(awsRuntimeFile)
        
    print("#Add the following to your /etc/hosts")
   
    for server in awsCluster['Servers']:
       name = server['Name']
       privateIP = server['PrivateIP']
       hostName = "ip-"+privateIP.replace('.','-')
       publicIP = awsRuntime[name]
       print(publicIP+" "+hostName)
