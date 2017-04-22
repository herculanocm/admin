angular.module('son')
    .config(['$stateProvider', '$urlRouterProvider', '$ocLazyLoadProvider', 'APP_REQUIRES', '$httpProvider', '$locationProvider',
        function ($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, appRequires, $httpProvider, $locationProvider) {


            // LAZY MODULES
            // -----------------------------------

            $ocLazyLoadProvider.config({
                debug: false,
                events: true,
                modules: appRequires.modules
            });


            // defaults to dashboard
            $urlRouterProvider.otherwise('/app/dashboard');
            //$urlRouterProvider.otherwise('/page/login');

            //
            // Application Routes
            // -----------------------------------

            $stateProvider
                .state('app', {
                    url: '/app',
                    abstract: true,
                    templateUrl: basepath('app.html'),
                    controller: 'AppController',
                    resolve: resolveFor('fastclick', 'animo', 'icons', 'modernizr', 'screenfull', 'animo', 'classyloader', 'csspiner')
                })
                .state('app.dashboard', {
                    url: '/dashboard',
                    title: 'Dashboard',
                    controller: 'DashController',
                    templateUrl: basepath('dashboard.html')
                })

                .state('app.profile', {
                    url: '/profile',
                    title: 'Profile',
                    templateUrl: basepath('profile.html'),
                    controller: 'ProfileController'
                })
                .state('app.404', {
                    url: '/404',
                    title: '404',
                    controller: '404Controller',
                    templateUrl: basepath('404.html')
                })
                .state('app.nroute', {
                    url: '/nroute',
                    title: 'Nenhuma Rota',
                    controller: 'NRouteController',
                    templateUrl: basepath('nroute.html')
                })
                .state('app.wellcome', {
                    url: '/wellcome',
                    title: 'Bem Vindo',
                    controller: 'WellcomeController',
                    templateUrl: basepath('wellcome.html')

                })
               
                
                // Sistema
                // -----------------------------------

                 .state('app.usuario', {
                    url: '/usuario',
                    title: 'Usuario',
                    controller: 'UsuarioController',
                    templateUrl: basepath('usuario.html')

                })
                 .state('app.roles', {
                    url: '/roles',
                    title: 'Roles',
                    controller: 'RolesController',
                    templateUrl: basepath('roles.html')

                })

                
                // Single Page Routes
                // -----------------------------------
                .state('page', {
                    url: '/page',
                    templateUrl: 'pages/page.html',
                    resolve: resolveFor('modernizr', 'icons')
                })
                .state('page.login', {
                    url: '/login',
                    title: "Login",
                    controller: 'LoginController',
                    templateUrl: 'pages/login.html'
                })
                .state('page.register', {
                    url: '/register',
                    title: "Register",
                    controller: 'RegistrerController',
                    templateUrl: 'pages/register.html'
                })
                .state('page.recover', {
                    url: '/recover',
                    title: "Recover",
                    controller: 'RecoverController',
                    templateUrl: 'pages/recover.html',
                    params: {
                        user: null
                    }
                })
                .state('page.lock', {
                    url: '/lock',
                    title: "Lock",
                    controller: 'LockController',
                    templateUrl: 'pages/lock.html'
                })
            ;

/*
            $httpProvider.interceptors.push(['$q', '$location', '$localStorage', function ($q, $location, $localStorage) {
                return {
                    'request': function (config) {
                        config.headers = config.headers || {};
                        if ($localStorage.token) {
                            config.headers.Authorization = 'Portador ' + $localStorage.token;
                        }
                        return config;
                    },
                    'responseError': function (response) {
                        if (response.status === 401 || response.status === 403) {
                            $location.path('/signin');
                        }
                        return $q.reject(response);
                    }
                };
            }]);

*/
            // for all app views
            function basepath(uri) {
                return 'views/' + uri;
            }

            // Generates a resolve object by passing script names
            // previously configured in constant.APP_REQUIRES
            function resolveFor() {
                var _args = arguments;
                return {
                    deps: ['$ocLazyLoad', '$q', function ($ocLL, $q) {
                        // Creates a promise chain for each argument
                        var promise = $q.when(1); // empty promise
                        for (var i = 0, len = _args.length; i < len; i++) {
                            promise = andThen(_args[i]);
                        }
                        return promise;

                        // creates promise to chain dynamically
                        function andThen(_arg) {
                            // also support a function that returns a promise
                            if (typeof _arg == 'function')
                                return promise.then(_arg);
                            else
                                return promise.then(function () {
                                    // if is a module, pass the name. If not, pass the array
                                    var whatToLoad = getRequired(_arg);
                                    // simple error check
                                    if (!whatToLoad) return $.error('Route resolve: Bad resource name [' + _arg + ']');
                                    // finally, return a promise
                                    return $ocLL.load(whatToLoad);
                                });
                        }

                        // check and returns required data
                        // analyze module items with the form [name: '', files: []]
                        // and also simple array of script files (for not angular js)
                        function getRequired(name) {
                            if (appRequires.modules)
                                for (var m in appRequires.modules)
                                    if (appRequires.modules[m].name && appRequires.modules[m].name === name)
                                        return appRequires.modules[m];
                            return appRequires.scripts && appRequires.scripts[name];
                        }

                    }]
                };
            }

            //$httpProvider.interceptors.push('APIInterceptor');

        }]);
