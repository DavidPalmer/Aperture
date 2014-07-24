import json,httplib,urllib,os
connection = httplib.HTTPSConnection('api.parse.com', 443)
connection.connect()
dir = "test"
for file in os.listdir(dir):
	connection.request('POST', '/1/files/' + file, open(dir + "/" + file, 'rb').read(), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "image/jpeg"
     })
	result = json.loads(connection.getresponse().read())
	fname = result["name"]
	furl = result["url"]
	print fname
	print furl
	connection.request('POST', '/1/classes/Photo', json.dumps({
       "photoFile": {
         "name": fname,
		 "url": furl,
         "__type": "File"
       }
     }), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "application/json"
     })
	result = json.loads(connection.getresponse().read())
	print result