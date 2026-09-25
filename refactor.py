import os
import re
import shutil

service_dir = r"src\main\java\org\example\aipoweredstudyresourcegenerator\service"
impl_dir = os.path.join(service_dir, "impl")

os.makedirs(impl_dir, exist_ok=True)

# List of services to process
services = [
    "DppService", "EmbeddingService", "NoteService", 
    "OpenAIService", "PineconeService", "QuestionGenerater", "TestService"
]

for svc in services:
    file_path = os.path.join(service_dir, f"{svc}.java")
    if not os.path.exists(file_path):
        continue
        
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # 1. Generate Interface
    # Extract package and imports
    package_match = re.search(r"package\s+[^;]+;", content)
    package_stmt = package_match.group(0) if package_match else ""
    
    imports = re.findall(r"import\s+[^;]+;", content)
    import_stmts = "\n".join(imports)
    
    # Remove @Service annotation from interface
    
    # Extract public methods (basic heuristic)
    methods = []
    # match public methods that don't look like constructors
    method_pattern = re.compile(r"public\s+([<>\w\s,\?]+)\s+(\w+)\s*\((.*?)\)\s*\{")
    for match in method_pattern.finditer(content):
        return_type = match.group(1).strip()
        name = match.group(2).strip()
        args = match.group(3).strip()
        if name != svc: # not a constructor
            methods.append(f"    {return_type} {name}({args});")
    
    # Check for records inside (like PineconeService.QueryMatch)
    records = []
    record_pattern = re.compile(r"public\s+record\s+\w+\(.*?\)\s*\{?\}?")
    for match in record_pattern.finditer(content):
        records.append(match.group(0))

    interface_content = f"{package_stmt}\n\n{import_stmts}\n\npublic interface {svc} {{\n"
    for m in methods:
        interface_content += f"{m}\n"
    for r in records:
        interface_content += f"    {r}\n"
    interface_content += "}\n"
    
    # 2. Generate Implementation
    impl_content = content
    # Change package
    impl_content = impl_content.replace(package_stmt, f"{package_stmt}\npackage org.example.aipoweredstudyresourcegenerator.service.impl;")
    impl_content = impl_content.replace(f"package org.example.aipoweredstudyresourcegenerator.service;\npackage org.example.aipoweredstudyresourcegenerator.service.impl;", "package org.example.aipoweredstudyresourcegenerator.service.impl;")
    
    # Add import for interface
    impl_content = impl_content.replace("import ", "import org.example.aipoweredstudyresourcegenerator.service." + svc + ";\nimport ", 1)
    
    # Change class declaration
    impl_content = re.sub(rf"public\s+class\s+{svc}", f"public class {svc}Impl implements {svc}", impl_content)
    
    # Change constructors
    impl_content = re.sub(rf"public\s+{svc}\s*\(", f"public {svc}Impl(", impl_content)
    
    # Add @Override to methods
    # (A bit complex via regex, but Java compiler doesn't strict require it. We will leave it out or just rely on Java's leniency)

    # 3. Write files
    impl_path = os.path.join(impl_dir, f"{svc}Impl.java")
    with open(impl_path, "w", encoding="utf-8") as f:
        f.write(impl_content)
        
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(interface_content)

print("Done")
