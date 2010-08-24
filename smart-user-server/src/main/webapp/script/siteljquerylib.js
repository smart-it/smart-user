
jQuery.fn.pagination = function(url, fragsLinkDivId) {
  var mainDiv = $(this)
  var mainDivId = mainDiv.attr('id');
  fetchContent("#"+mainDivId, url, fragsLinkDivId);
};

function fetchContent(mainDivId, url, fragsLinkDivId) {
  $.ajax({
    type: "GET",
    url: url,
    dataType: "html",
    success: function(html) {
      //      putting html data in id div
      $(mainDivId).html(html);
      cacheContent(mainDivId, fragsLinkDivId);
    },
    error: function(xhr){
      alert(window.location);
      alert(xhr.status);

    }
  });
}

function cacheContent(mainDivId, fragsLinkDivId) {
  //      making next and previos link inactive
  $("div#"+fragsLinkDivId).find('a').each(function(){
    var thisLink = $(this);
    $.ajax({
      type: "GET",
      url: this.toString(),
      dataType: "html",
      success: function(html){
        var divCount = $(html).find('div').length
        if(divCount <= 4) {
          $(thisLink).hide()
        }
        else {
          var href = $(thisLink).attr('href');
          $(thisLink).click(function(){
            fetchContent(mainDivId, href, fragsLinkDivId);
            return false;
          });
        }
      },
      error: function(xhr){
      }
    });
  //    end of  making next and previos link inactive
  });
}
