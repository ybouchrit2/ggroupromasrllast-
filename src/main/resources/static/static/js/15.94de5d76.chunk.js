"use strict";(self.webpackChunkggroup=self.webpackChunkggroup||[]).push([[15],{2015:(e,s,a)=>{a.r(s),a.d(s,{default:()=>c});var r=a(2483),t=a(4686),n=a(4666),l=a(6723);const c=()=>{const[e,s]=(0,r.useState)(""),[a,c]=(0,r.useState)(""),[i,d]=(0,r.useState)(""),[o,u]=(0,r.useState)(!1),h=(0,n.Zp)();return(0,l.jsx)("div",{className:"container mt-5",children:(0,l.jsx)("div",{className:"row justify-content-center",children:(0,l.jsx)("div",{className:"col-md-6",children:(0,l.jsxs)("div",{className:"card",children:[(0,l.jsx)("div",{className:"card-header",children:(0,l.jsx)("h3",{children:"\u062a\u0633\u062c\u064a\u0644 \u0627\u0644\u062f\u062e\u0648\u0644"})}),(0,l.jsxs)("div",{className:"card-body",children:[i&&(0,l.jsx)("div",{className:"alert alert-danger",children:i}),(0,l.jsxs)("form",{onSubmit:async s=>{s.preventDefault(),u(!0),d("");try{const s=await t.A.post("/api/users/signin",{email:e,password:a});if(s.data&&s.data.token){const e=s.data.token;localStorage.setItem("token",e);const a=JSON.parse(atob(e.split(".")[1])).userId;if(!a)return d("\u062a\u0639\u0630\u0631 \u0627\u0633\u062a\u062e\u0631\u0627\u062c \u0645\u0639\u0631\u0641 \u0627\u0644\u0645\u0633\u062a\u062e\u062f\u0645 \u0645\u0646 \u0627\u0644\u062a\u0648\u0643\u0646."),void u(!1);const r=(await t.A.get("/api/users/".concat(a),{headers:{Authorization:"Bearer ".concat(e)}})).data.role;"ROLE_ADMIN"===r?h("/dashboardadmin"):"ROLE_USER"===r?h("/UserDashboard"):d("\u062f\u0648\u0631 \u0627\u0644\u0645\u0633\u062a\u062e\u062f\u0645 \u063a\u064a\u0631 \u0645\u0639\u0631\u0648\u0641.")}else d("\u062a\u0639\u0630\u0631 \u0627\u0644\u062d\u0635\u0648\u0644 \u0639\u0644\u0649 \u0627\u0644\u062a\u0648\u0643\u0646 \u0645\u0646 \u0627\u0644\u062e\u0627\u062f\u0645.")}catch(r){r.response?404===r.response.status?d("\u0627\u0644\u0645\u0633\u062a\u062e\u062f\u0645 \u063a\u064a\u0631 \u0645\u0648\u062c\u0648\u062f"):401===r.response.status?d("\u0643\u0644\u0645\u0629 \u0627\u0644\u0645\u0631\u0648\u0631 \u063a\u064a\u0631 \u0635\u062d\u064a\u062d\u0629"):d("\u062e\u0637\u0623 \u0641\u064a \u0627\u0644\u062e\u0627\u062f\u0645: ".concat(r.response.status,". \u064a\u0631\u062c\u0649 \u0627\u0644\u0645\u062d\u0627\u0648\u0644\u0629 \u0644\u0627\u062d\u0642\u0627\u064b.")):r.request?d("\u0641\u0634\u0644 \u0627\u0644\u0627\u062a\u0635\u0627\u0644 \u0628\u0627\u0644\u062e\u0627\u062f\u0645. \u064a\u0631\u062c\u0649 \u0627\u0644\u0645\u062d\u0627\u0648\u0644\u0629 \u0644\u0627\u062d\u0642\u0627\u064b."):d("\u062d\u062f\u062b \u062e\u0637\u0623 \u063a\u064a\u0631 \u0645\u062a\u0648\u0642\u0639 \u0623\u062b\u0646\u0627\u0621 \u062a\u0633\u062c\u064a\u0644 \u0627\u0644\u062f\u062e\u0648\u0644.")}finally{u(!1)}},children:[(0,l.jsxs)("div",{className:"form-group",children:[(0,l.jsx)("label",{htmlFor:"email",children:"\u0627\u0644\u0628\u0631\u064a\u062f \u0627\u0644\u0625\u0644\u0643\u062a\u0631\u0648\u0646\u064a"}),(0,l.jsx)("input",{type:"email",className:"form-control",id:"email",value:e,onChange:e=>s(e.target.value),required:!0})]}),(0,l.jsxs)("div",{className:"form-group",children:[(0,l.jsx)("label",{htmlFor:"password",children:"\u0643\u0644\u0645\u0629 \u0627\u0644\u0645\u0631\u0648\u0631"}),(0,l.jsx)("input",{type:"password",className:"form-control",id:"password",value:a,onChange:e=>c(e.target.value),required:!0})]}),(0,l.jsx)("button",{type:"submit",className:"btn btn-primary w-100",disabled:o,children:o?"\u062c\u0627\u0631\u064a \u062a\u0633\u062c\u064a\u0644 \u0627\u0644\u062f\u062e\u0648\u0644...":"\u062a\u0633\u062c\u064a\u0644 \u0627\u0644\u062f\u062e\u0648\u0644"})]})]}),(0,l.jsx)("div",{className:"card-footer text-center",children:(0,l.jsxs)("p",{children:["\u0644\u0627 \u062a\u0645\u0644\u0643 \u062d\u0633\u0627\u0628\u061f ",(0,l.jsx)("a",{href:"/signup",children:"\u062a\u0633\u062c\u064a\u0644 \u062c\u062f\u064a\u062f"})]})})]})})})})}}}]);
//# sourceMappingURL=15.94de5d76.chunk.js.map