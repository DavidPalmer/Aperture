import json,httplib,urllib
connection = httplib.HTTPSConnection('api.parse.com', 443)
connection.connect()
'''
connection.request('POST', '/1/classes/TestObject', json.dumps({
       "foo": "bar"
     }), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "application/json"
     })
result = json.loads(connection.getresponse().read())
params = urllib.urlencode({"count":1,"limit":0})
connection.request('GET', '/1/classes/TestObject?%s' % params, '', {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML"
     })
result = json.loads(connection.getresponse().read())
print result
print result["count"]
'''
params = urllib.urlencode({"limit":200})
connection.request('GET', '/1/classes/Photo?%s' % params, '', {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML"
     })
result = json.loads(connection.getresponse().read())
dict = {}
for res in result["results"]:
	print res["photoFile640"]["url"]
	