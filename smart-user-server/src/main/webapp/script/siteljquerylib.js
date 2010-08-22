jQuery.fn.pagination = function(url,id) {
  url = url.toString();
  var prevUrl = url;
  url = url.replace("?","/frags?");
  if(url == prevUrl)
    url = url+"/frags";
  $.ajax({
    type: "GET",
    url: url,
    dataType: "text/html",
    success: function(html) {      
      $(id).html(html);     

    },
    error: function(xhr){
      alert(window.location);
      alert(xhr.status);

    }
        
  });
};