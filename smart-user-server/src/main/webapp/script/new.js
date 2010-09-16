/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
if (document.all&&window.img_sitel_logo){
var x=new Array()
var direction=new Array()
var y=new Array()
if (img_sitel_logo.length==null){
img_sitel_logo[0]=document.all.img_sitel_logo
x[0]=0
direction[0]="right"
y[0]=img_sitel_logo[0].height
img_sitel_logo[0].filters.light.addPoint(100,50,100,255,255,255,90)
}
else
for (i=0;i<img_sitel_logo.length;i++){
x[i]=0
direction[i]="right"
y[i]=img_sitel_logo[i].height
img_sitel_logo[i].filters.light.addPoint(100,50,100,255,255,255,90)
}
}

function slidelight(cur){
img_sitel_logo[cur].filters.light.MoveLight(0,x[cur],y[cur],200,-1)

if (x[cur]<img_sitel_logo[cur].width+200&&direction[cur]=="right")
x[cur]+=10
else if (x[cur]>img_sitel_logo[cur].width+200){
direction[cur]="left"
x[cur]-=10
}
else if (x[cur]>-200&&x[cur]<-185){
direction[cur]="right"
x[cur]+=10
}
else{
x[cur]-=10
direction[cur]="left"
}
}

if (document.all&&window.img_sitel_logo){
if (img_sitel_logo.length==null)
setInterval("slidelight(0)",img-sitel-logo[0].speed)
else
for (t=0;t<img_sitel_logo.length;t++){
var temp='setInterval("slidelight('+t+')",'+img_sitel_logo[t].speed+')'
eval(temp)
}
}

