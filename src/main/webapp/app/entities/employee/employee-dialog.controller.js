(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'Address'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, Address) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addresses = Address.query({filter: 'employee-is-null'});
        $q.all([vm.employee.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.employee.address || !vm.employee.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.employee.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('newApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
