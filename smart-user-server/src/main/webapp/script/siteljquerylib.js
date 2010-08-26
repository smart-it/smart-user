
jQuery.fn.pagination = function(url, fragsLinkDivId, divno) {
  divno = (typeof divno == "undefined")?4:divno;
  var mainDiv = $(this)
  var mainDivId = mainDiv.attr('id');
  fetchContent("#"+mainDivId, url, fragsLinkDivId, divno);
};

function fetchContent(mainDivId, url, fragsLinkDivId, divno) {
  $.ajax({
    type: "GET",
    url: url,
    dataType: "html",
    success: function(html) {
      //      putting html data in id div
      $(mainDivId).html(html);
      cacheContent(mainDivId, fragsLinkDivId, divno);
    },
    error: function(xhr){
      alert(window.location);
    }
  });
}

function cacheContent(mainDivId, fragsLinkDivId, divno) {
  //      making next and previos link inactive
  $("div#"+fragsLinkDivId).find('a').each(function(){
    var thisLink = $(this);
    $.ajax({
      type: "GET",
      url: this.toString(),
      dataType: "html",
      success: function(html){
        var divCount = $(html).find('div').length;
        if(divCount <= divno) {
          $(thisLink).hide()
        }
        else {
          var href = $(thisLink).attr('href');
          $(thisLink).click(function(){
            fetchContent(mainDivId, href, fragsLinkDivId,divno);
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
