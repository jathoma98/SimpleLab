TEMPLATE = {
    WORKSPACE_EQM_ELEM:
        '<div class="ui-widget-content fixed draggable_item center">' +
        '   <img src="../../img/cylinder.png" class="item_img">' +
        '   <p>{{name}}</p>' +
        '</div>',
    WORKSPACE_EQM_ELEM_BAR:
        '<div class="row sidebaritem">\n' +
        '    <div class="col s4">\n' +
        '        <img src="../../img/beaker.png" class="item_img">\n' +
        '    </div>\n' +
        '    <div class="col s4">\n' +
        '        <p>Name: {{equipment.name}}</p>\n' +
        '        <p>Type: {{equipment.type}}</p>\n' +
        '    </div>\n' +
        '    {{^equipment.machine}}\n' +
        '    <div class="col s4">\n' +
        '       {{#equipment.liquid}}\n' +
        '       <p>Current Volume: {{curr_val}}</p>\n' +
        '       {{/equipment.liquid}}\n' +
        '       {{#equipment.solid}}\n' +
        '       <p>Current Weight: {{curr_val}}</p>\n' +
        '       {{/equipment.solid}}\n' +
        '       <p>Current Temperature: {{equipment.properties.min_temperature}} - {{properties.max_temperature}}</p>\n' +
        '    </div>\n' +
        '    {{/equipment.machine}}\n' +
        '    {{#equipment.machine}}\n' +
        '    <div class="col s4"></div>\n' +
        '    {{/equipment.machine}}\n' +
        '</div>'
}
