angular.module('son', ['ui.router', 'oc.lazyLoad', 'ngStorage', 'ngCookies', 'ui.bootstrap','ui.utils.masks', 'ngAnimate',
    'angular-loading-bar','toaster','ngTouch','angularMoment','ngSanitize', 'ngCsv','ngResource','cgBusy','ngFileUpload'])
    .run(['$rootScope', '$state', '$stateParams', '$window', '$location', '$uibModal', 'AuthService','APP_MENUS',
        function ($rootScope, $state, $stateParams, $window, $location, $modal, AuthService,APP_MENUS) {

    	
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            $rootScope.$storage = $window.localStorage;
           


            
            $rootScope.app = {
                name: 'ONlineADM',
                description: 'CRM completo para Vendas',
                year: ((new Date()).getFullYear()),
                layout: {
                    isFixed: true,
                    isCollapsed: false,
                    isBoxed: false,
                    isRTL: false
                },
                viewAnimation: 'ng-fadeInUp'
            };


            $rootScope.warn = function (message, title, sucessCallback) {
                var modalInstance = $modal.open({
                    template: ' <div class="modal-header"><h3>{{title}}</h3></div><div class="modal-body">{{message}}</div><div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">OK</button></div>',
                    controller: function ($scope, $uibModalInstance) {
                        $scope.message = message;
                        $scope.title = title || "Erro";
                        $scope.ok = function () {
                            $uibModalInstance.close();
                        };
                    }
                });
                modalInstance.result.then(sucessCallback);
            };

            $rootScope.alert = function (message, title, sucessCallback) {
                var modalInstance = $modal.open({
                    template: ' <div class="modal-header"><h3>{{title}}</h3></div><div class="modal-body">{{message}}</div><div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">OK</button></div>',
                    controller: function ($scope, $uibModalInstance) {
                        $scope.message = message;
                        $scope.title = title || "Alerta";
                        $scope.ok = function () {
                            $uibModalInstance.close();
                        };
                    }
                });

                modalInstance.result.then(sucessCallback);
            };

            $rootScope.confirm = function (message, positiveCallback, negativeCallback, title) {
                $modal.open({
                    template: ' <div class="modal-header" ng-style="{\'z-index\': 99000}"><h3>{{title}}</h3></div><div class="modal-body">{{message}}</div><div class="modal-footer"><button class="btn btn-primary" ng-click="sim()">Sim</button><button class="btn btn-danger" ng-click="nao()">Não</button></div>',
                    controller: function ($scope, $uibModalInstance) {
                        $scope.message = message;
                        $scope.title = title || "Confirmação";
                        $scope.sim = function () {
                            $uibModalInstance.result.then(positiveCallback);
                            $uibModalInstance.close();
                        };
                        $scope.nao = function () {
                            $uibModalInstance.result.then(negativeCallback);
                            $uibModalInstance.close();
                        };
                    }
                });
            };

        }
    ]);