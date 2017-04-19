angular
    .module('son')
    .controller(
        'AppController',
        [
            '$rootScope',
            '$scope',
            '$state',
            '$window',
            '$localStorage',
            '$timeout',
            'toggleStateService',
            'colors',
            'cfpLoadingBar',
            'AuthService',
            function ($rootScope, $scope, $state, $window,
                      $localStorage, $timeout, toggle, colors,
                      cfpLoadingBar, AuthService) {
                "use strict";
                //console.log('carregando appcontroller');

                $scope.isNavCollapsed = true;
                $scope.isCollapsed = true;
                $scope.isCollapsedHorizontal = false;
                $scope.user = AuthService.getUserSessionStorage();

                

                 if (!$scope.user || $scope.user == null || $scope.user == undefined) {
                 $state.go('page.login', '', {notify: false}).then(function () {
                	 console.log('foi para o login');
                 $rootScope.$broadcast('$stateChangeSuccess');
                 });
                 }
                 

                var thBar;
                $rootScope.$on('$stateChangeStart', function (event,
                                                              toState, toParams, fromState, fromParams) {
                    //console.log('Alterando status Status Atual:' + fromState.name + '        Status Final:' + toState.name);

                    if ($('.wrapper > section').length) {
                        thBar = $timeout(function () {

                            cfpLoadingBar.start();
                        }, 0); // sets a latency Threshold
                    }

                    
                     if (!AuthService.isAuth()) {

                     //console.log('Usuario não esta logado');
                     //$location.path('/page/login');
                     event.preventDefault();

                     $state.go('page.login', toParams, {notify: false}).then(function () {
                     $rootScope.$broadcast('$stateChangeSuccess', toState, toParams, fromState, fromParams);
                     });

                     } else {
                     return;
                     }
                     
                     /*
                      * else if (!AuthService.isMenuState(toState.name) && AuthService.isEstadosPadroes(toState.name)) {
                     event.preventDefault();
                     $state.go('app.nroute', toParams, {notify: false}).then(function () {
                     $rootScope.$broadcast('$stateChangeSuccess', toState, toParams, fromState, fromParams);
                     });

                     } 
                      * */
                });

                $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState,
                                                                fromParams) {

                    event.targetScope.$watch("$viewContentLoaded",
                        function () {

                            $timeout.cancel(thBar);
                            cfpLoadingBar.complete();
                        });

                });

                // Hook not found
                $rootScope.$on('$stateNotFound', function (event,
                                                           unfoundState, fromState, fromParams) {
                    //console.log(unfoundState.to); // "lazy.state"
                    //console.log(unfoundState.toParams); // {a:1, b:2}
                    //console.log(unfoundState.options); // {inherit:false} + default options
                });

                // Hook success
                $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState,
                                                                fromParams) {
                    // display new view from top
                    $window.scrollTo(0, 0);
                    // Save the route title
                    $rootScope.currTitle = $state.current.title;
                });

                $rootScope.currTitle = $state.current.title;
                $rootScope.pageTitle = function () {
                    return $rootScope.app.name
                        + ' - '
                        + ($rootScope.currTitle || $rootScope.app.description);
                };

                // iPad may presents ghost click issues
                // if( ! browser.ipad )
                // FastClick.attach(document.body);

                // Close submenu when sidebar change from collapsed to normal
                $rootScope
                    .$watch(
                        'app.layout.isCollapsed',
                        function (newValue, oldValue) {
                            if (newValue === false)
                                $rootScope
                                    .$broadcast('closeSidebarMenu');
                        });

                // Restore layout settings
                if (angular.isDefined($localStorage.layout))
                    $scope.app.layout = $localStorage.layout;
                else
                    $localStorage.layout = $scope.app.layout;

                $rootScope.$watch("app.layout", function () {
                    $localStorage.layout = $scope.app.layout;
                }, true);

                // Allows to use branding color with interpolation
                // {{ colorByName('primary') }}
                $scope.colorByName = colors.byName;

                // Hides/show user avatar on sidebar

                $scope.toggleUserBlock = function () {
                    ////console.log('passou no toggleUserBlock');
                    $scope.$broadcast('toggleUserBlock');
                    $scope.isCollapsed = !$scope.isCollapsed;

                    ////console.log('Colapse esta : '+$scope.isCollapsed);
                };

                // Internationalization
                // ----------------------

                // Restore application classes state
                toggle.restoreState($(document.body));

            }])
    .controller(
        'DashController',
        [
            '$scope',
            '$rootScope',
            '$log',
            '$http',
            '$state',
            function ($scope, $rootScope, $log, $http,$state) {

                $scope.chartVendedores = {};
                $scope.chartVendedores.type = 'BarChart';
                $scope.chartVendedores.showRowNumber = true;
                $scope.chartVendedores.showValue = true;

                $scope.produto = {
                    title: "POST COM ANGULAR",
                    description: "ANGULAR POST WEBSTORM COM SPRING BOOT",
                    pages: 29,
                    precos: [{
                        value: 9,
                        bookType: "EBOOK"
                    }]
                };

                /*
                 var respostaUni = $http.get('http://localhost:8080/produtos/list');
                 respostaUni.then(function (resp) {
                 var resultado = resp.data;

                 console.log('resposta lista ' + JSON.stringify(resp));

                 }, function (error) {
                 $log.error('Eror ' + error);
                 $rootScope.warn('ERRO ', 'ATENÇÃO', function () {
                 //console.log('mensagem enviadoa');
                 });
                 });
                 */

                $scope.enviarDados = function () {
                	console.log('enviando dados');
                	$state.go('app.veiculos');
                };

            }])
    .controller(
        'LoginController',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            '$localStorage',
            '$resource',
            'UsuarioService',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state, $localStorage,$resource,UsuarioService) {
                console.log('iniciou o controler login');

                AuthService.logout();

                /*
                 Metodo de Login
                 */
                $scope.findUser = function (login) {
                    //console.log('usuario e senha digitados '+JSON.stringify(login));
                 
                    var respostaUni = AuthService.login(login);
                respostaUni.then(function (resp) {
                       var usuario= resp.data;

                       AuthService.setUserSessionStorage(usuario);
                       
                       if(typeof(login.lembrar) != "undefined" && login.lembrar == true){
                    	   AuthService.setUserLocalStorage(usuario);
                       }
                       
                       $state.go('app.dashboard');

                    },
                    function (error) {
                        $log.error('Eror '
                            + JSON.stringify(error));
                        $rootScope.warn('ERRO '+error.data.descricao, 'ATENÇÃO',
                            function () {
                                //console.log('mensagem enviadoa');
                            });
                    });

                   
                };

            }])
    .controller(
        'SidebarController',
        [
            '$rootScope',
            '$scope',
            '$state',
            '$location',
            '$http',
            '$timeout',
            'APP_MEDIAQUERY',
            'AuthService',
            'APP_MENUS',
            function ($rootScope, $scope, $state, $location, $http,
                      $timeout, mq, AuthService, APP_MENUS) {
                //console.log('carregando sidebar');
                var currentState = $rootScope.$state.current.name;

                var $win = $(window);
                var $html = $('html');
                var $body = $('body');

                $scope.menuItensSidebar = APP_MENUS;

                // Adjustment on route changes
                $rootScope.$on('$stateChangeStart', function (event,
                                                              toState, toParams, fromState, fromParams) {
                    currentState = toState.name;
                    // Hide sidebar automatically on mobile
                    $('body.aside-toggled').removeClass(
                        'aside-toggled');

                    $rootScope.$broadcast('closeSidebarMenu');

                });

                // Normalize state on resize to avoid multiple checks
                $win.on('resize', function () {
                    if (isMobile())
                        $body.removeClass('aside-collapsed');
                    else
                        $body.removeClass('aside-toggled');
                });

                // Check item and children active state
                var isActive = function (item) {

                    if (!item)
                        return;

                    if (!item.sref || item.sref == '#') {
                        var foundActive = false;
                        angular.forEach(item.submenu, function (value, key) {
                            if (isActive(value))
                                foundActive = true;
                        });
                        return foundActive;
                    } else
                        return $state.is(item.sref);
                };

                // Load menu from json file
                // -----------------------------------

                $scope.getMenuItemPropClasses = function (item) {
                    return (item.heading ? 'nav-heading' : '')
                        + (isActive(item) ? ' active' : '');
                };

                // Handle sidebar collapse items
                // -----------------------------------
                var collapseList = [];
                var collapseListItem = [];

                $scope.alterCollapseItem = function ($index) {
                    $event.stopPropagation();
                    collapseList[$index] = !collapseList[$index];
                };

                $scope.addCollapse = function ($index, item) {
                    collapseList[$index] = !isActive(item);
                };
                $scope.addCollapseItem = function ($index, item) {
                    // //console.log('Item +'+JSON.stringify(item)+' posicao '+$index);
                    collapseListItem[$index] = !isActive(item);
                    ////console.log(JSON.stringify(collapseListItem));
                };

                $scope.isCollapse = function ($index) {
                    return (collapseList[$index]);
                };
                $scope.isCollapseItem = function ($index) {
                    ////console.log('item '+$index+' '+JSON.stringify(collapseListItem[$index]));
                    return (collapseListItem[$index]);
                };

                $scope.toggleCollapse = function ($index) {
                    //console.log('entrou');
                    // collapsed sidebar doesn't toggle drodopwn
                    if (isSidebarCollapsed() && !isMobile())
                        return true;
                    // make sure the item index exists
                    if (typeof collapseList[$index] === undefined)
                        return true;

                    closeAllBut($index);
                    collapseList[$index] = !collapseList[$index];

                    return true;

                    function closeAllBut($index) {
                        angular.forEach(collapseList,
                            function (v, i) {
                                if ($index !== i)
                                    collapseList[i] = true;
                            });
                    }
                };
                $scope.toggleCollapseItem = function ($index) {
                    // //console.log('entrou');
                    // collapsed sidebar doesn't toggle drodopwn
                    if (isSidebarCollapsed() && !isMobile()) {
                        ////console.log('primeiro if');
                        return true;
                    }
                    // make sure the item index exists
                    if (typeof collapseListItem[$index] === undefined) {
                        ////console.log('segundo if');
                        return true;
                    }

                    closeAllBut($index);
                    collapseListItem[$index] = !collapseListItem[$index];
                    ////console.log('INDEX' + $index + ' collapse ' + collapseListItem[$index]);
                    return true;

                    function closeAllBut($index) {
                        ////console.log('entrou no for');
                        angular.forEach(collapseListItem, function (v, i) {
                            if ($index !== i)
                                collapseListItem[i] = true;
                        });
                    }
                };

                // Helper checks
                // -----------------------------------

                function isMobile() {
                    return $win.width() < mq.tablet;
                }

                function isTouch() {
                    return $html.hasClass('touch');
                }

                function isSidebarCollapsed() {
                    return $body.hasClass('aside-collapsed');
                }

                function isSidebarToggled() {
                    return $body.hasClass('aside-toggled');
                }

            }])
    .controller('UserBlockController', ['$scope', function ($scope) {
        ////console.log('passou no UserBlockController');
        $scope.userBlockVisible = true;

        $scope.$on('toggleUserBlock', function (event, args) {

            $scope.userBlockVisible = !$scope.userBlockVisible;

        });

    }])
    .controller(
        'ProfileController',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            'APP_END_POINT',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state, APP_END_POINT) {
                //console.log('iniciou o controler de profile');
                var usuarioRoot = AuthService
                    .getUserSessionStorage();
                $scope.usuarioProfile = usuarioRoot;

                //$scope.usuarioProfile.desSenha1=usuarioRoot.desSenha;
                //$scope.usuarioProfile.desSenha2=usuarioRoot.desSenha;
                $scope.usuarioProfile.nome2 = usuarioRoot.nome;
                $scope.usuarioProfile.desEmail2 = usuarioRoot.desEmail;

                $scope.addUsuarioProfile = function (usuarioProfile) {
                    var resposta = AuthService
                        .setUsuarioProfile(usuarioProfile);
                    resposta
                        .then(
                            function (resp) {
                                var resultado = resp.data;
                                var user = resultado.data;
                                ////console.log(JSON.stringify(resultado));

                                if (resultado.type) {

                                    AuthService
                                        .setUserSessionStorage(user);

                                    $rootScope
                                        .alert(
                                            ''
                                            + resultado.des,
                                            'OK',
                                            function () {
                                                //console.log('mensagem enviadoa');
                                            });

                                } else {
                                    //AuthService.setUserLocalStorage(null);
                                    $rootScope
                                        .warn(
                                            ''
                                            + resultado.des,
                                            'ATENÇÃO',
                                            function () {
                                                //console.log('mensagem enviadoa');
                                            });
                                }

                            },
                            function (error) {
                                $log.error('Eror ' + error);
                            });

                };

            }])
    .controller(
        'WellcomeController',
        [
            '$scope',
            'toaster',
            'PessoaService',
            '$log',
            '$http',
            function ($scope, toaster, PessoaService, $log, $http) {

                $scope.highlightFilteredHeader = function (row,
                                                           rowRenderIndex, col, colRenderIndex) {
                    if (col.filters[0].term) {
                        return 'header-filtered';
                    } else {
                        return '';
                    }
                };

                $scope.gridOptions = {
                    enableFiltering: true,
                    onRegisterApi: function (gridApi) {
                        $scope.gridApi = gridApi;
                    },
                    columnDefs: [{
                        field: 'name'
                    }, {
                        field: 'gender',
                        visible: false
                    }, {
                        field: 'company'
                    }],
                    data: [{
                        name: 'Herculano',
                        gender: 'Macho',
                        company: 'Algar tech'
                    }, {
                        name: 'Jeferson',
                        gender: 'Macho',
                        company: 'Algar tech'
                    }],
                    enableGridMenu: true,
                    enableSelectAll: true,
                    exporterCsvFilename: 'myFile.csv',
                    exporterPdfDefaultStyle: {
                        fontSize: 9
                    },
                    exporterPdfTableStyle: {
                        margin: [30, 30, 30, 30]
                    },
                    exporterPdfTableHeaderStyle: {
                        fontSize: 10,
                        bold: true,
                        italics: true,
                        color: 'red'
                    },
                    exporterPdfHeader: {
                        text: "My Header",
                        style: 'headerStyle'
                    },
                    exporterPdfFooter: function (currentPage,
                                                 pageCount) {
                        return {
                            text: currentPage.toString() + ' of '
                            + pageCount.toString(),
                            style: 'footerStyle'
                        };
                    },
                    exporterPdfCustomFormatter: function (docDefinition) {
                        docDefinition.styles.headerStyle = {
                            fontSize: 22,
                            bold: true
                        };
                        docDefinition.styles.footerStyle = {
                            fontSize: 10,
                            bold: true
                        };
                        return docDefinition;
                    },
                    exporterPdfOrientation: 'portrait',
                    exporterPdfPageSize: 'LETTER',
                    exporterPdfMaxGridWidth: 500,
                    exporterCsvLinkElement: angular
                        .element(document
                            .querySelectorAll(".custom-csv-link-location")),
                    onRegisterApi: function (gridApi) {
                        $scope.gridApi = gridApi;
                    }
                };

                /////////////////////////////////////////////////////////////////// autocomplete ////////////////////////////

                $scope.getPessoas = function (val) {
                    return PessoaService.getPessoasURLAsync(val);
                };

            }])
    .controller('NullController', ['$scope', function ($scope) {
    }])

    .controller(
        'RecoverController',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            '$stateParams',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state, $stateParams) {
                $scope.usuarioRecover = AuthService
                    .getUserSessionStorage();
                //console.log('iniciou o controler de alteração de recovery');

                if (!$scope.usuarioRecover) {
                    $state.go('page.login');
                }

                $scope.resetSenha = function (userSenha) {
                    $scope.usuarioRecover.desSenha = userSenha.senha2;
                    var resposta = AuthService
                        .setResetSenha($scope.usuarioRecover);
                    resposta.then(function (resp) {
                        var resultado = resp.data;

                        if (resultado.type) {

                            AuthService.alterPass(userSenha);
                            $state.go('app.wellcome');
                        } else {
                            $rootScope.warn('' + resultado.des,
                                'ATENÇÃO', function () {
                                    //console.log('mensagem enviadoa');
                                });
                        }

                    }, function (error) {

                    });
                };

            }])
    .controller(
        '404Controller',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state) {
                //console.log('iniciou o controler 404');

            }])
    .controller(
        'NRouteController',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state) {
                //console.log('iniciou o controler de n route');

            }])
    .controller(
        'LockController',
        [
            '$scope',
            'AuthService',
            '$window',
            '$rootScope',
            '$log',
            '$state',
            function ($scope, AuthService, $window, $rootScope,
                      $log, $state) {

                AuthService.block();

                $scope.desbloquear = function (senha) {
                    if (AuthService.unBlock(senha)) {
                        $state.go('app.wellcome');
                    } else {
                        $rootScope.warn('Senha incorreta!',
                            'ATENÇÃO', function () {
                                //console.log('mensagem enviadoa');
                            });
                    }
                };

            }])


;

/*
 .controller('RelMovimentoController', ['$scope', '$log', '$state', 'toaster', '$rootScope',
 function ($scope, $log, $state, toaster, $rootScope) {


 }])
 */

/*
 calendario


 $scope.today = function () {
 $scope.movimento.dta_inclusao = new Date();
 };
 $scope.today();

 $scope.clear = function () {
 $scope.movimento.dta_inclusao = null;
 };

 $scope.inlineOptions = {
 customClass: getDayClass,
 minDate: new Date(),
 showWeeks: true
 };

 $scope.dateOptions = {
 dateDisabled: disabled,
 formatYear: 'yyyy',
 maxDate: new Date(2020, 5, 22),
 minDate: new Date(),
 startingDay: 1
 };

 // Disable weekend selection
 function disabled(data) {
 var date = data.date,
 mode = data.mode;
 return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
 }

 $scope.toggleMin = function () {
 $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
 $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
 };

 $scope.toggleMin();

 $scope.open1 = function () {
 $scope.popup1.opened = true;
 };

 $scope.open2 = function () {
 $scope.popup2.opened = true;
 };

 $scope.setDate = function (year, month, day) {
 $scope.dt = new Date(year, month, day);
 };

 $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'dd/MM/yyyy', 'shortDate'];
 $scope.format = $scope.formats[3];
 $scope.altInputFormats = ['M!/d!/yyyy'];

 $scope.popup1 = {
 opened: false
 };

 $scope.popup2 = {
 opened: false
 };

 var tomorrow = new Date();
 tomorrow.setDate(tomorrow.getDate() + 1);
 var afterTomorrow = new Date();
 afterTomorrow.setDate(tomorrow.getDate() + 1);
 $scope.events = [
 {
 date: tomorrow,
 status: 'full'
 },
 {
 date: afterTomorrow,
 status: 'partially'
 }
 ];

 function getDayClass(data) {
 var date = data.date,
 mode = data.mode;
 if (mode === 'day') {
 var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

 for (var i = 0; i < $scope.events.length; i++) {
 var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

 if (dayToCheck === currentDay) {
 return $scope.events[i].status;
 }
 }
 }

 return '';
 }


 */