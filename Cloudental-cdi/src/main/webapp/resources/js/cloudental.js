

function toggleShowHideHtmlElement(id) {
	var element = document.getElementById(id);
	if(element.style.display == 'block') {
		element.style.display = 'none';
	} else {
		element.style.display = 'block';
	}
}