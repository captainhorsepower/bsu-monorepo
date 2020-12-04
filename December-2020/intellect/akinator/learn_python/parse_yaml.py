import yaml # PyYaml docs: https://pyyaml.org/wiki/PyYAMLDocumentation

a_yaml_file = open("example.yaml")
parsed_yaml_file = yaml.load(a_yaml_file, Loader=yaml.FullLoader)

print(f'dict: \n{parsed_yaml_file["a_dictionary"]}')
print(f'list: \n{parsed_yaml_file["a_list"]}')