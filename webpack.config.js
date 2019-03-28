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
            'stompjs': __dirname + '/node_modules' + '/stompjs/lib/stomp.js'
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
            },
            {
                test: /\.scss$/,
                exclude: /(node_modules)/,
                // include: [
                //     path.resolve(__dirname, 'src/main/resources/static/sass')
                // ],
                // test: /\.\/src\/main\/resources\/static\/sass\/style\.scss/,
                // test: path.join(__dirname, '.'),
                // test: path.join(__dirname, 'src/main/resources/static/sass/.'),
                use: [{
                    loader: 'style-loader' // inject CSS to page
                }, {
                    loader: 'css-loader' // translates CSS into CommonJS modules
                }, {
                    loader: 'postcss-loader', // Run postcss actions
                    options: {
                        plugins: function() { // postcss plugins, can be exported to postcss.config.js
                            return [
                                require('autoprefixer')
                            ];
                        }
                    }
                }, {
                    loader: 'sass-loader' // compiles Sass to CSS
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
            },
            {
                from: './node_modules/bootstrap/dist/css/bootstrap.min.css',
                to: './src/main/resources/static/css/bootstrap.min.css'
            },
            {
                from: './node_modules/bootstrap/dist/css/bootstrap-grid.min.css',
                to: './src/main/resources/static/css/bootstrap-grid.min.css'
            },
            {
                from: './node_modules/bootstrap/dist/css/bootstrap-reboot.min.css',
                to: './src/main/resources/static/css/bootstrap-reboot.min.css'
            }
            // },
            // {
            //     from: './node_modules/react-bootstrap/dist/react-bootstrap.min.js',
            //     to: './src/main/resources/static/js/react-bootstrap.min.js'
        ])
    ]
};
