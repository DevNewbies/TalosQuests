var enums = require("ui/enums");
function pattern(time,i){
	return time*i;
}
function curveAnimation(page,time,x,y){
	page.animate({
		opacity:1,
		translate: { x: x, y: y},
        duration: time,
        curve: enums.AnimationCurve.easeIn
    });
}
function fadeAnimation(page,time){
		page.animate({
		    opacity:1,
		    duration: time
    	});
}
exports.fadeAnimation = function(page,time){
	 page.animate({
        opacity:1,
        duration: time
    });
}
exports.curveAnimation = function(page,time,x,y){
	page.animate({
		opacity:1,
		translate: { x: x, y:y},
        duration: time,
        curve: enums.AnimationCurve.linear
    });
}
exports.multiFadeAnimation = function (page,id,layout,time){
	var lay = page.getViewById(layout);
	console.log(lay.getChildrenCount());
	for(var i=0; i<=lay.getChildrenCount()-1;i++){
		var pat = pattern(time,i);
		fadeAnimation(page.getViewById(id+i),pat);
	}
}
exports.multiCurveAnimation= function(page,id,layout,time,x,y){
	var lay = page.getViewById(layout);
	console.log(lay.getChildrenCount());
	for(var i=0; i<=lay.getChildrenCount()-1;i++){
		var pat = pattern(time,i);
		curveAnimationAnimation(page.getViewById(id+i),pat,x,y);
	}
}