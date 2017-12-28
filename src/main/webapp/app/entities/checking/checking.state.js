(function() {
    'use strict';

    angular
        .module('newApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checking', {
            parent: 'entity',
            url: '/checking',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'newApp.checking.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checking/checkings.html',
                    controller: 'CheckingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checking');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checking-detail', {
            parent: 'checking',
            url: '/checking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'newApp.checking.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checking/checking-detail.html',
                    controller: 'CheckingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checking');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Checking', function($stateParams, Checking) {
                    return Checking.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checking',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checking-detail.edit', {
            parent: 'checking-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checking/checking-dialog.html',
                    controller: 'CheckingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Checking', function(Checking) {
                            return Checking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checking.new', {
            parent: 'checking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checking/checking-dialog.html',
                    controller: 'CheckingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('checking', null, { reload: 'checking' });
                }, function() {
                    $state.go('checking');
                });
            }]
        })
        .state('checking.edit', {
            parent: 'checking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checking/checking-dialog.html',
                    controller: 'CheckingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Checking', function(Checking) {
                            return Checking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checking', null, { reload: 'checking' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checking.delete', {
            parent: 'checking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checking/checking-delete-dialog.html',
                    controller: 'CheckingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Checking', function(Checking) {
                            return Checking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checking', null, { reload: 'checking' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
