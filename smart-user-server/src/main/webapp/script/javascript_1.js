/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */








//var ajaxRequest = null;
////var url ='http://feeds.sophos.com/en/atom1_0-sophos-security-news.xml';
//var url = 'http://localhost:9090/orgs/SITEL/users';
//
//window.onload = doRequest;
//
//function doRequest()
//{
//	if (null == ajaxRequest)
//	{
//		ajaxRequest = new XMLHttpRequest();
//	}
//	ajaxRequest.onreadystatechange = ReqStateChange;
//	ajaxRequest.open( 'GET', url, true );
//        ajaxRequest.setRequestHeader('Accept', 'application/atom+xml')
//	ajaxRequest.send( null );
//
//}
//// state handler
//function ReqStateChange()
//{
//	// if request ready
//	if ( ajaxRequest.readyState == 4 )
//	{
//		if ( ajaxRequest.status == 200 )
//		{
//			loadContent( ajaxRequest );
//		}
//		else
//		{
//			alert( "Error, could not load content " + ajaxRequest.status );
//		}
//	}
//}
//function loadContent(argReq)
//{
//
//	var XMLData = null;
//	var title="";
//	var id="";
//	var summary="";
//
//	XMLData =  argReq.responseXML.documentElement;
//
//	if (XMLData != null)
//	{
//            alert(title);
//
//                var entryNodes = XMLData.getElementsByTagName('entry');
//		var titleNodes = XMLData.getElementsByTagName('title');
//                var idNodes = XMLData.getElementsByTagName('id');
//		var summaryNodes = XMLData.getElementsByTagName('summary');
//                //alert(entryNodes)
//                for (i = 0; i < entryNodes.length; i++){
//                    title += entryNodes[i].childNodes[1].textContent + '<br />';
//                    alert(title);
//                    id +=entryNodes[i].childNodes[0].textContent + '<br />';
//                    alert(id)
//                }
//
////		for (i = 0; i < titleNodes.length; i++)
////		{
////                    if(titleNodes[i].childNodes.length < 1) {
////			title += titleNodes[i].nodeValue + '<br />';
////                    }
////                    else {
////                        title += titleNodes[i].childNodes[0].nodeValue + '<br />';
////                    }
////		}
//
////		for (i = 0; i < idNodes.length; i++)
////		{
////			id += idNodes[i].childNodes[0].nodeValue + '<br />';
////		}
////		for (i = 0; i < summaryNodes.length; i++)
////		{
////			summary += summaryNodes[i].childNodes[0].nodeValue + '<br />';
////		}
//
//	}
//
//       	document.getElementById("teblecontentid").innerHTML = title;
//}





function Orgpageselect()
{

    var className=document.getElementById("showList").className;
//    alert(className);
//    var className1=document.getElementById("create").className;
//    alert(className1);
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
//
//


function postwith (to) {
  var myForm = document.createElement("form");
  myForm.method="post" ;
  myForm.action = to ;
//  for (var k in p) {
//    var myInput = document.createElement("input") ;
//    myInput.setAttribute("name", k) ;
//    myInput.setAttribute("value", p[k]);
//    myForm.appendChild(myInput) ;
//  }
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

alert('js loaded');
//$(document).ready(function(){
//
//    $.ajax({
//        type: "GET",
//        url: "http://localhost:9090/orgs/SITEL/users",
//        datatype: "xml",
//        success: function(xml){
//            alert(xml)
//            var title ="";
//            var id = "";
//            $(xml).find('entry').each(function(){
//                title = $(this).find('title').text();
//                alert(title);
//                alert("dfasd");
//                alert($(this).find('id').text());
//            });
//        }
//    });
//});