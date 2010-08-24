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

