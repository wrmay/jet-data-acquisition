{
    "EnvironmentName" : "{{ EnvironmentName }}",
    "RegionName" : "{{ RegionName }}",
    "KeyPair" : "{{ SSHKeyPairName }}",
    "SSHKeyPath" : "{{ SSHKeyPath }}",
    {% if Environment %}
    "Environment" : {
    {% for key,d in Environment.items() %}
      "{{ key }}" : {
    {%for k,v in d.items() %}
          "{{ k }}" : "{{ v }}" {% if not loop.last -%},{% endif %}
    {% endfor %}
  } {% if not loop.last -%},{%- endif %}
    {% endfor %}
    },
    {% endif %}
    "Servers" : [
    {% for Server in Servers %}
        {
            "Name" : "{{ Server.Name }}",
            "ImageId" : "{{ Server.ImageId }}",
            "InstanceType" : "{{ Server.InstanceType }}",
            "PrivateIP" : "{{ Server.PrivateIP }}",
            "AZ" : "{{ Server.AZ }}",
            "SSHUser" : "ec2-user",
            "XMX" : "{{ Server.XMX }}",
            "XMN" : "{{ Server.XMN }}",
            "Roles" : [ {% for Role in Server.Roles -%}"{{ Role }}"{%if not loop.last -%},{%- endif %}{%- endfor %}],
            "BlockDevices" : [
            ],
            "Installations" : []
        } {%if not loop.last -%},{%- endif %}
        {% endfor %}
    ]
}
