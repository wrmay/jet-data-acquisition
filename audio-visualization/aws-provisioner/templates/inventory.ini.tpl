{% for server in Servers %}
{{ server.PublicIpAddress }}
{% endfor %}

[locators]
{% for server in Servers if 'Locator' in server.Roles %}
{{ server.PublicIpAddress }}
{% endfor %}

[datanodes]
{% for server in Servers if 'DataNode' in server.Roles %}
{{ server.PublicIpAddress }}
{% endfor %}

[etls]
{% for server in Servers if 'ETL' in server.Roles %}
{{ server.PublicIpAddress }}
{% endfor %}