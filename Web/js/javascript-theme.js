var width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
var height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
var displayxxx = 0;

function getCurrWidth()
{
  return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
}

function getCurrHeight()
{
  return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
}

function getBrowserWidth()
{
  return window.outerWidth;
}

function getBrowserHeight()
{
  return window.outerWidth;
}

function Ready()
{
  $('.content-body').scrollspy({ target: '#header-nav' });
  
  $('.carousel').carousel({
    interval: 4000
  });
}

function scroll()
{
}

$(document).ready(Ready);
