/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//uzzal(start)

$( init );

function init() {

  $('#right').append( $('.leftmenu') );
}

$(document).ready(function () {
  $(".leftmenu_body ul li").hover(function () {
    $(this).css({
      'background-color' : '#F7F5FE',
      'color' : 'Black'
    });
  }, function () {
    var cssObj = {
      'background-color' : '',
      'color' : '#23819C'
    }
    $(this).css(cssObj);
  });

});

$(document).ready(function () {
  $(".leftmenu_body ul li a").hover(function () {
    $(this).css({
      'color' : 'orange',
      'font-weight' : 'bolder',
      'font-size':'11pt'
    });
  }, function () {
    var cssObj = {
      'background-color' : '',
      'font-weight' : '',
      'font-size':'10pt',
      'color' : '#23819C'
    }
    $(this).css(cssObj);
  });

});


//uzzal(End)
//saumitra(start)



$(document).ready(function(){



  var url=window.location.toString();

  var prevUrl = url;
  url = url.replace("?","/frags?");
  if(url == prevUrl)
    url = url+"/frags";

  $("#tablecontentid").pagination(url, "paginationLinks");

  
  $("#uniqueShortName").keyup(function(){

    var usn =$("#uniqueShortName").val();

    $.ajax({
      type: "GET",
      url: "http://localhost:9090/orgs/shortname/"+usn,
      dataType: "xml",
      success: function(xhr){
        //                            alert('Short Name is not unique')
        $("#alertlabel").html('Short Name is not unique: try another');
      },
      error: function(xhr){
        $("#alertlabel").html('Perfect: Carry On');

      }

    });



  });
      
  $("#organizationform").validate({
    rules: {
      name: "required",
      streetAddress: "required",
      city: "required",
      state: "required",
      country:"required",
      zip:"required",


      uniqueShortName: "required"

    }


  });



});
//saumitra(end)
//Rocky(start)
