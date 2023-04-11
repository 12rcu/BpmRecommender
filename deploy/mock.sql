INSERT INTO bpm_items (name, description)
VALUES ('BPMN 2.0', 'graphical notation based on flowcharting techniques'),
       ('Declarative', 'implicit annotation with constrains'),
       ('eGantt', 'extension of the Gantt Chart with an emphasis to capture time characteristics'),
       ('EPC', 'planning of implementation and configuration of resources in a enterprise setting'),
       ('Flow', 'Flowcharts as a easy to understand BPMN'),
       ('IDEF3',
        'modeling of processes with a focus on the process flow as well as the state of objects and respective conditions'),
       ('Petri Net', 'used for describing distributed systems and has a mathematical theory'),
       ('UML Act.', 'documents activity and control flow as a float chart');

INSERT INTO bpm_item_categories (name, description, value_range)
VALUES ('mental_load', 'The mental load on a user when using the BPMN, types in {undefined, easy, hard}', 2),
       ('enterprise', 'Category that indicates if the BPMN should be used for enterprise projects', 1),
       ('use_case', 'Indicate if the Notation has support for use_case', 1),
       ('activities', 'Indicates if the Notation has support for activities', 1),
       ('color', 'Indicates if the Notation {forbid, allow, require} colors', 2);