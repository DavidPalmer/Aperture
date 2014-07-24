
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.beforeSave("Photo", function(request, response) {
	var photoObj = request.object;
	var query = new Parse.Query("Photo");
	query.count({
		success: function(count) {
			console.log("Count Retrieved");
			photoObj.set("photoNo", (count + 1));
			var fname = photoObj.get("photoFile").name();
			fname = fname.substring(42, fname.indexOf("."));
			console.log("PhotoFile: " + fname);
			var Image = require("parse-image");
			Parse.Cloud.httpRequest({
				url: photoObj.get("photoFile").url()
			  }).then(function(response) {
				var image = new Image();
				return image.setData(response.buffer);
			  }).then(function(image) {
				var scaleRatio = 640.0 / Math.max(640, image.width());
				console.log("Scaling.." + scaleRatio);
				return image.scale({
				  ratio: scaleRatio
				});
			  }).then(function(image) {
				// Make sure it's a JPEG to save disk space and bandwidth.
				return image.setFormat("JPEG");
			  }).then(function(image) {
				// Get the image data in a Buffer.
				return image.data();
			  }).then(function(buffer) {
				// Save the image into a new file.
				var base64 = buffer.toString("base64");
				var cropped = new Parse.File(fname + "_640.jpg", { base64: base64 });
				return cropped.save();
			  }).then(function(cropped) {
				// Attach the image file to the original object.
				photoObj.set("photoFile640", cropped);
			  }).then(function(result) {
					Parse.Cloud.httpRequest({
					url: photoObj.get("photoFile").url()
				  }).then(function(response) {
					var image = new Image();
					return image.setData(response.buffer);
				  }).then(function(image) {
					var scaleRatio = 1024.0 / Math.max(1024, image.width());
					console.log("Scaling.." + scaleRatio);
					return image.scale({
					  ratio: scaleRatio
					});
				  }).then(function(image) {
					// Make sure it's a JPEG to save disk space and bandwidth.
					return image.setFormat("JPEG");
				 
				  }).then(function(image) {
					// Get the image data in a Buffer.
					return image.data();
				  }).then(function(buffer) {
					// Save the image into a new file.
					var base64 = buffer.toString("base64");
					var cropped = new Parse.File(fname + "_1024.jpg", { base64: base64 });
					return cropped.save();
				  }).then(function(cropped) {
					// Attach the image file to the original object.
					photoObj.set("photoFile1024", cropped);
				  }).then(function(result) {
					response.success();
				  }, function(error) {
					response.error(error);
				  });
			  }, function(error) {
				response.error(error);
			  });
		},
		error: function(error) {
			response.error(error);
		}
	});
});
