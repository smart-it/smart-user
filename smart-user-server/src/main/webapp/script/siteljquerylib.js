jQuery.fn.pagination = function(url,id) {
  alert(url);
  alert(id)
  $.ajax({
    type: "GET",
    url: url,
    dataType: "text/html",
    success: function(html) {      
      $(id).html(html);
      alert(id);

    },
    error: function(xhr){
      alert(window.location);
      alert(xhr.status);

    }
        
  });
};