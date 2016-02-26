var yaml = require('js-yaml'),
  util = require('util'),
  isDate = util.isDate,
  rYFM = /^(?:-{3,}\s*\n+)?([\s\S]+?)(?:\n+-{3,})(?:\s*\n+([\s\S]*))?/;

exports = module.exports = function(str){
  return parse(str);
};

var split = exports.split = function(str){
  if (!rYFM.test(str)) return {content: str};

  var match = str.match(rYFM),
    data = match[1],
    content = match[2] || '';

  return {data: data, content: content};
};

var escapeYaml = exports.escape = function(str){
  return str.replace(/\n(\t+)/g, function(match, tabs){
    var result = '\n';

    for (var i = 0, len = tabs.length; i < len; i++){
      result += '  ';
    }

    return result;
  });
};

var parse = exports.parse = function(str, options){
  var splitData = split(str),
    raw = splitData.data,
    content = splitData.content;

  if (!raw) return {_content: str};

  try {
    var data = yaml.load(escapeYaml(raw), options),
      keys = Object.keys(data),
      key,
      date;

    // Convert timezone
    for (var i = 0, len = keys.length; i < len; i++){
      key = keys[i];

      if (isDate(data[key])){
        date = data[key];
        data[key] = new Date(date.getTime() + date.getTimezoneOffset() * 60 * 1000);
      }
    }

    if (typeof data === 'object'){
      data._content = content;
      return data;
    } else {
      return {_content: str};
    }
  } catch (e){
    return {_content: str};
  }
};

var formatNumber = function(num){
  return (num < 10 ? '0' : '') + num;
};

var formatDate = function(date){
  return date.getFullYear() + '-' +
    formatNumber(date.getMonth() + 1) + '-' +
    formatNumber(date.getDate()) + ' ' +
    formatNumber(date.getHours()) + ':' +
    formatNumber(date.getMinutes()) + ':' +
    formatNumber(date.getSeconds());
};

exports.stringify = function(obj, options){
  var content = obj._content || '',
    keys = Object.keys(obj);

  if (!keys.length) return content;

  var data = {},
    nullKeys = [],
    dateKeys = [],
    i,
    len,
    key,
    value;

  for (i = 0, len = keys.length; i < len; i++){
    key = keys[i];
    if (key === '_content') continue;
    value = obj[key];

    if (value == null){
      nullKeys.push(key);
    } else if (value instanceof Date){
      dateKeys.push(key);
    } else {
      data[key] = value;
    }
  }

  if (!Object.keys(data).length) return content;

  var result = yaml.dump(data, options);

  if (dateKeys.length){
    var date;

    for (i = 0, len = dateKeys.length; i < len; i++){
      key = dateKeys[i];
      result += key + ': ' + formatDate(obj[key]) + '\n';
    }
  }

  if (nullKeys.length){
    for (i = 0, len = nullKeys.length; i < len; i++){
      result += nullKeys[i] + ':\n';
    }
  }

  result += '---\n' + content;

  return result;
};