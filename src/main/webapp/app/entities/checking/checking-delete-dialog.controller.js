(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('CheckingDeleteController',CheckingDeleteController);

    CheckingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Checking'];

    function CheckingDeleteController($uibModalInstance, entity, Checking) {
        var vm = this;

        vm.checking = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Checking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
