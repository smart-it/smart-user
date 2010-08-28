/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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