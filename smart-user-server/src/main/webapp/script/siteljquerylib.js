jQuery.fn.pagination = function(url,id) {
    
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