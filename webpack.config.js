// Webpack config was written by: https://spring.io/guides/tutorials/react-and-spring-data-rest/
var path = require('path');
var CopyPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',
    resolve: {
        alias: {
            'stompjs': __dirname + '/node_modules' + '/stompjs/lib/stomp.js',
        }
    },
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }]
            }
        ]
    },
    plugins: [
        // Copy built JS file from build location into where Spring Boot gradle runner servers it. This allows hot-reload of any JavaScript (and only javascript! no static files)
        new CopyPlugin([ // See: https://webpack.js.org/plugins/copy-webpack-plugin/
            {
                from: './src/main/resources/static/built/bundle.js',
                to: './build/resources/main/static/built/bundle.js'
            }
        ])
    ]
};
