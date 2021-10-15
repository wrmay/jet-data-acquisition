# Overview #

Provisions a t3.micro in its own vpc on AWS.

# To Do
Use some sort of process monitoring for the bridge so we know if its
really up. (Monit, supervisord)

# Setup #
* Requires Ansible

Install the ansible prometheus plugin: https://github.com/cloudalchemy/ansible-prometheus
using ansible-galaxy

If running a recent MacOs, this role will not work unless you also do the following:
- export OBJC_DISABLE_INITIALIZE_FORK_SAFETY=YES
- install gnu-tar (not just any tar!)


* The local machine requires python3 to be installed and the following python packages
 * jina2
 * boto3
 * awscli

* You will need to register a key pair with AWS and you will need the
corresponding .pem file on your local machine.
* You will need an AWS AccessKeyId and SecretAccessKey. You can either use your
master key (which is discouraged by Amazon) or create an IAM user.  The IAM
user you create will need to attach a user policy that grants access to all EC2, Cloud Formation and Elastic Load Balancing operations.  The following
policy definition can be used.

    ```json
    {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Action": [
                    "ec2:*",
                    "cloudformation:*",
                    "elasticloadbalancing:*"
                ],
                "Effect": "Allow",
                "Resource": "*"
            }
        ]
    }
    ```

__For detailed AWS setup instructions, see "AWS_Setup.docx"__

# Setting up an AWS Cluster #
Configuring a cluster starts with a cluster configuration file like the
one below.  Only the most basic information is provided in this file. All
of the other configurations are provided by a set of templates called a
"plan".  A plan is an opinionated approach to AWS deployment.


Here is a sample cluster definition file for a 3 node cluster.
```
{
  "EnvironmentName" : "Test",
  "RegionName" : "us-east-2",
  "SSHKeyPairName" : "lab-keypair",
  "SSHKeyPath": "/Users/me/Downloads/lab-keypair.pem",
  "Servers" : [
    {
      "Name" : "gem1101",
        "PrivateIP" : "192.168.1.101",
        "AZ" : "A",
        "InstanceType" : "m4.xlarge",
        "Roles" : ["DataNode", "Locator"]
    },
    {
      "Name" : "gem2101",
      "PrivateIP" : "192.168.2.101",
      "AZ" : "B",
      "InstanceType" : "m4.xlarge",
      "Roles" : ["DataNode", "Locator"]
    },
    {
      "Name" : "gem3101",
      "PrivateIP" : "192.168.3.101",
      "AZ" : "C",
      "InstanceType" : "m4.xlarge",
      "Roles" : ["DataNode", "Locator"]
    }
  ]
}

```
This file must be named "awscluster.json" and it must be in the "config"
directory. Once you have created your awscluster.json, generate all of the
configurations by running "generateAWSCluster.py".  You will need to read
"Instance Types Supported by the Default Plan" and
"Networking and the Default Plan" below to understand what options are
supported.

The setup steps are shown in concise form below:

```
cd ~/gem-ops-suite
cp samples/awscluster.json config
vi config/awscluster.json  #edit the cluster definition
python3 generateAWSCluster.py
```

You now have a completely reproducible GemFire cluster on AWS.  The entire
project can be checked in to source control to allow versioning of the cluster.

# Scripts #

- __aws_provision_storage.py__

  Provisions the EBS volumes for your cluster.

- __aws_destroy_storage.py__

   Destroys the EBS volumes for your cluster.  __This will cause data loss.__

- __aws_provision.py__

  Provisions the networking and EC2 instances.

- __setup.py__

  Installs all software and configurations onto the EC2 instances.  Note it
  is harmless to run this script multiple times.  Updates to GemFire cluster
  configurations will be propagated each time this is run.

