# Front-matter

[![Build Status](https://travis-ci.org/hexojs/front-matter.svg?branch=master)](https://travis-ci.org/hexojs/front-matter)  [![NPM version](https://badge.fury.io/js/hexo-front-matter.svg)](http://badge.fury.io/js/hexo-front-matter)

Front-matter parser.

## API

### parse(str, [options])

Parses YAML front-matter. See [js-yaml] for more info.

### stringify(obj, [options])

Converts an object to YAML front-matter string. See [js-yaml] for more info.

### split(str)

Splits a YAML front-matter string.

### escape(str)

Converts hard tabs to soft tabs.

## License

MIT

[js-yaml]: https://github.com/nodeca/js-yaml