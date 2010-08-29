/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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