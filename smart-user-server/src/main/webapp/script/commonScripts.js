/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//uzzal(start)

$( init );

function init() {

  $('#left').append( $('.leftmenu') );
}

$(document).ready(function () {
  $(".leftmenu_body ul li").hover(function () {
    $(this).css({
//      'background-color' : '#F7F5FE',
'background-color' : '#EEEEFF',
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

function showonlyone(thechosenone) {
  var newboxes = document.getElementsByTagName("div");
  for(var x=0; x<newboxes.length; x++) {
    name = newboxes[x].getAttribute("name");
    if (name == 'newboxes') {
      if (newboxes[x].id == thechosenone) {
        newboxes[x].style.display = 'block';
      }
      else {
        newboxes[x].style.display = 'none';
      }
    }
  }
}
//uzzal(End)

function Orgpageselect()
{

  var className=document.getElementById("showList").className;
  if(className=="show")
  {
    document.getElementById("showList").className="hide";
    document.getElementById("create").className="show";
  }
  else
  {
    document.getElementById("showList").className="show";
    document.getElementById("create").className="hide";
  }
}

function postwith (to) {
  var myForm = document.createElement("form");
  myForm.method="post" ;
  myForm.action = to ;
  document.body.appendChild(myForm) ;
  myForm.submit() ;
  document.body.removeChild(myForm) ;
}

function PostAsJson()
{
  $(document).ready(function(){
    alert('Potakkkkkkkkkkkkkkkk');
    $('form').submit(function(e) {
      e.preventDefault();
      $.post($(this).attr("action"), $(this).serialize(), function(json) {
        // handle response
        }, "json");
    });
  });
}