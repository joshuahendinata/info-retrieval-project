var map;
var markerMap = {};

$(document).ready(function(){
	
	var windowHeight = $(window).height();
	var bodyPos = $("#content-body").position();
	var cat = "all";
	var homeQ = (window.location.href).replace(/[#]/g,'');
	console.log(homeQ);

	// To be used later in scroll end function
	var curSelection = homeQ + "/cat/all";
	
	//console.log("query is: " + (window.location.href).match(/query\/([a-z0-9]*)?[#/]/)[1]);
	// Trigger window resize for once
	$(window).resize();
	
	// Load more when it reaches the end
	$(".nano").bind("scrollend", function(e){
	    console.log("current HTMLDivElement", window.location.href);

		$.get(curSelection, function(data){
		    	var testObj = data;

		    	// Just append with json format and use data.response
		    	$(".nano-content").append(testObj);
				$(".nano").nanoScroller();
				$(".tweet-message").linkify({
					  target: '_blank'
				});
				
				$(".test").each(function(){
					// Map the tweets' version field with its markerInfoPair
					markerMap[$(this).attr('version')] = createMarker($(this));
				});
		}); 
	});

	// Request the tweets based on category button clicked
	$("body").on("click", ".cat", function(){
		// To block the button when clicked twice
		if(cat === $(this).attr("id")) return
		else{
			cat = $(this).attr("id");
			curSelection = homeQ + "/cat/" + cat;

			// to mark the active one
			$('.cat').removeClass("active");
			$(this).addClass("active");
			
			//$(".nano-content").html(eval(curSelection));
			$.get(curSelection, function(data){
	
				console.log("request this: " + curSelection);
				$(".nano-content").html(data);
				$(".nano").nanoScroller();
				$(".tweet-message").linkify({
					  target: '_blank'
				});
				
				initializeGMap();
			}); 
		}
	});
	
	$("body").on("click", ".loc", function(){
		// To block the button when clicked twice
		if(cat === $(this).attr("id")) return
		else{
			cat = $(this).attr("id");

			// to mark the active one
			$('.loc').removeClass("active");
			$('.loc-dropdown').removeClass("active");
			$(this).addClass("active");
			
			//$(".nano-content").html(eval(curSelection));
			$.get(homeQ + "/loc/" + cat, function(data){
				
				console.log("request this: " + homeQ + "/loc/" + cat);

				// Replace category button group with latest AJAX call
				$("#cat-button").html($(data).find("#cat-button").html());
				
				// Replace the tweets message
				$(".nano-content").html($(data).find(".nano-content").html());
				
				$(".nano").nanoScroller();
				$(".tweet-message").linkify({
					  target: '_blank'
				});

				$('.cat:eq(0)').addClass("active");
				initializeGMap();
			}); 
		}
	});
	
	$("body").on("click", ".loc-drop", function(){
		// To block the button when clicked twice
		if(cat === $(this).attr("id")) return
		else{
			cat = $(this).attr("id");

			// to mark the active one
			$('.loc').removeClass("active");
			
			$(".loc-dropdown").html($(this).text() + 
					"<span data-dropdown='drop'></span>");
			$(".loc-dropdown").addClass("active loc-drop");
			$(".loc-dropdown").attr("id", cat);
			$(document).foundation();
			
			//$(".nano-content").html(eval(curSelection));
			$.get(homeQ + "/loc/" + cat, function(data){
				
				console.log("request this: " + homeQ + "/loc/" + cat);

				// Replace category button group with latest AJAX call
				$("#cat-button").html($(data).find("#cat-button").html());
				
				// Replace the tweets message
				$(".nano-content").html($(data).find(".nano-content").html());
				
				$(".nano").nanoScroller();
				$(".tweet-message").linkify({
					  target: '_blank'
				});

				$('.cat:eq(0)').addClass("active");
				initializeGMap();
			}); 
		}
	});
	
	// click tweets, scroll Google map, and highlight it
	$("body").on('click', '.test', function(){
		map.panTo(new google.maps.LatLng(
			$(this).attr('lat'), 
			$(this).attr('lng')));
		map.setZoom(12);
		
		var marker = markerMap[$(this).attr('version')][0];
		var infoWindow = markerMap[$(this).attr('version')][1];
		
		// open the infoWindow associated with this marker
		infoWindow.open(map, marker);
		
		$('.test').css({"background-color":"white"});
		$(this).css({"background-color":"#AED4FF"});
	});
	

	// Submit button
	$("#search-btn").click(function(){
		$("#main-query").submit();
	});
	
	//Refresh button
	$("#refresh-btn").click(function(){
		NProgress.configure({ trickleRate: 0.07, trickleSpeed: 800 });
		NProgress.start();
		$.get("http://localhost:8080/assignment/refresh", function(data){
			NProgress.done();
			alert("New tweets loaded!");
		});
	});
	
	// First letter category to upper case
	$(".cat").css('textTransform', 'capitalize');
	
	// ================= Linkify plugin method =======================
	$(".tweet-message").linkify({
		  target: '_blank'
	})
		
	// ================= Google Map API initializer ==================
	google.maps.event.addDomListener(window, 'load', initializeGMap);

});

$(window).resize(function(){
	try{
		var windowHeight = $(window).height();
		var bodyPos = $("#content-body").position();

		// Resize twitter content div
		$("#content-body").height(windowHeight - bodyPos.top - 10);

		// Reset nano scroller
		$(".nano").nanoScroller();
	}catch(err){
		console.log(err.meesage)
	}
});

function initializeGMap() {
	  var mapOptions = {
	    zoom: 1,
	    center: new google.maps.LatLng(
	    		$('.test:eq(0)').attr('lat'), 
	    		$('.test:eq(0)').attr('lng'))
	  };
	
	  // create map
	  map = new google.maps.Map(document.getElementById('tweet-map'),
	  mapOptions);
	  
	  $(".test").each(function(){
		  // map the version with markerInfoPair
		  markerMap[$(this).attr('version')] = createMarker($(this));
	  });
}

function createMarker(tweetObj){	
	  // adding marker to individual tweets
	  var marker = new google.maps.Marker({
		  position: new google.maps.LatLng(
			  tweetObj.attr('lat'), 
			  tweetObj.attr('lng')),
			  map: map,
			  title: tweetObj.attr('version')
	  });
	
	  // create window
	  var infowindow = new google.maps.InfoWindow({
		  content: tweetObj.children('span.user-id')[0].outerHTML
		  			+ tweetObj.children('.tweet-message')[0].outerHTML
	  });
	
	  var markerInfoPair = [marker, infowindow];
	  
	  // add click listener to the marker
	  google.maps.event.addListener(marker, 'click', function() {
		  map.setZoom(12);
		  map.setCenter(marker.getPosition());
		  infowindow.open(map,marker);
		  $(".nano").nanoScroller({ scrollTo: tweetObj });
		  $('.test').css({"background-color":"white"});
		  tweetObj.css({"background-color":"#AED4FF"});
	  });
	  
	  return markerInfoPair;
}

