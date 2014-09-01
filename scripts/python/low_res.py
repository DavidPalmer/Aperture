import json,httplib,urllib,os
connection = httplib.HTTPSConnection('api.parse.com', 443)
connection.connect()
params = urllib.urlencode({"limit":200})
connection.request('GET', '/1/classes/Photo?%s' % params, '', {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML"
     })
result = json.loads(connection.getresponse().read())
dict = {}
for res in result["results"]:
	name = res["photoFile"]["name"]
	c = name.index(".")
	dict[name[42:c]] = res["objectId"]
directory="Stock_Original"
dir_640="640"
dir_1024="1024"
list = []
for file in os.listdir(directory):
	fname = file[:file.index(".")]
	fname_1024 = fname + "_1024.jpg"
	fname_640 = fname + "_640.jpg"
	print fname, fname_1024, fname_640
	connection.request('POST', '/1/files/' + fname_640, open(dir_640 + "/" + fname_640, 'rb').read(), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "image/jpeg"
     })
	result = json.loads(connection.getresponse().read())
	fname_640_upl = result["name"]
	connection.request('POST', '/1/files/' + fname_1024, open(dir_1024 + "/" + fname_1024, 'rb').read(), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "image/jpeg"
     })
	result = json.loads(connection.getresponse().read())
	fname_1024_upl = result["name"]
	print fname_640_upl, fname_1024_upl
	if (fname in dict):
		objId = dict[fname]
		connection.request('PUT', '/1/classes/Photo/%s' % objId, json.dumps({
       "photoFile640": {
         "name": fname_640_upl,
         "__type": "File"
       },
	   "photoFile1024": {
         "name": fname_1024_upl,
         "__type": "File"
       }
         }), {
       "X-Parse-Application-Id": "dUB6bvVXgV2jeEwABqDGuZs1hKUaEOtP01wfM0SN",
       "X-Parse-REST-API-Key": "kzYgYhY994q0GEWZwgA0VBmKC9axqJfOjlGPyIML",
       "Content-Type": "application/json"
        })
		result = json.loads(connection.getresponse().read())
	
	